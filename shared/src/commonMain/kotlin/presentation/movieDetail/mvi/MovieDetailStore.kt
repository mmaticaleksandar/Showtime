package presentation.movieDetail.mvi

import core.result.AppResult
import data.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailStore(
    private val moviesRepository: MoviesRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var observeMovieDetailJob: Job? = null
    private var observeFavoriteJob: Job? = null
    private var observeWatchlistJob: Job? = null
    private var observeCastJob: Job? = null

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MovieDetailEffect>()
    val effect: SharedFlow<MovieDetailEffect> = _effect.asSharedFlow()

    private fun reduce(change: MovieDetailChange) {
        _state.value = MovieDetailReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    fun onIntent(intent: MovieDetailIntent) {
        when (intent) {
            is MovieDetailIntent.LoadMovieDetail -> {
                reduce(MovieDetailChange.LoadingStarted)

                observeMovieDetail(intent.movieId)
                observeFavorite(intent.movieId)
                observeWatchlist(intent.movieId)
                observeCast(intent.movieId)

                syncMovieDetail(intent.movieId)
            }

            MovieDetailIntent.ToggleFavorite -> {
                toggleFavorite()
            }

            MovieDetailIntent.ToggleWatchlist -> {
                toggleWatchlist()
            }
        }
    }

    private fun observeMovieDetail(movieId: String) {
        observeMovieDetailJob?.cancel()

        observeMovieDetailJob = scope.launch {
            moviesRepository
                .observeMovieDetail(movieId)
                .collectLatest { movie ->
                    reduce(
                        MovieDetailChange.MovieChanged(
                            movie = movie
                        )
                    )
                }
        }
    }

    private fun observeCast(movieId: String) {
        observeCastJob?.cancel()

        observeCastJob = scope.launch {
            moviesRepository
                .observeMovieCast(movieId)
                .collectLatest { cast ->
                    reduce(
                        MovieDetailChange.CastChanged(
                            cast = cast
                                .map { it.name }
                                .distinct()
                                .take(10)
                        )
                    )
                }
        }
    }

    private fun observeFavorite(movieId: String) {
        observeFavoriteJob?.cancel()

        observeFavoriteJob = scope.launch {
            moviesRepository
                .observeIsFavorite(movieId)
                .collectLatest { isFavorite ->
                    reduce(
                        MovieDetailChange.FavoriteChanged(
                            isFavorite = isFavorite
                        )
                    )
                }
        }
    }

    private fun observeWatchlist(movieId: String) {
        observeWatchlistJob?.cancel()

        observeWatchlistJob = scope.launch {
            moviesRepository
                .observeIsInWatchlist(movieId)
                .collectLatest { isInWatchlist ->
                    reduce(
                        MovieDetailChange.WatchlistChanged(
                            isInWatchlist = isInWatchlist
                        )
                    )
                }
        }
    }

    private fun syncMovieDetail(movieId: String) {
        scope.launch {
            reduce(MovieDetailChange.LoadingStarted)

            when (val result = moviesRepository.syncMovieDetail(movieId)) {
                is AppResult.Success -> {
                    reduce(MovieDetailChange.DetailSyncSucceeded)
                }

                is AppResult.Error -> {
                    val hasCachedMovie = _state.value.movie != null

                    reduce(
                        MovieDetailChange.DetailLoadFailed(
                            message = result.message,
                            hasCachedMovie = hasCachedMovie
                        )
                    )
                }
            }
        }
    }

    private fun toggleFavorite() {
        val movieId = _state.value.movie?.imdbId ?: return
        val currentlyFavorite = _state.value.isFavorite

        scope.launch {
            when (
                val result = moviesRepository.toggleFavorite(
                    movieId = movieId,
                    currentlyFavorite = currentlyFavorite
                )
            ) {
                is AppResult.Success -> Unit

                is AppResult.Error -> {
                    _effect.emit(
                        MovieDetailEffect.ShowMessage(result.message)
                    )
                }
            }
        }
    }

    private fun toggleWatchlist() {
        val movieId = _state.value.movie?.imdbId ?: return
        val currentlyInWatchlist = _state.value.isInWatchlist

        scope.launch {
            when (
                val result = moviesRepository.toggleWatchlist(
                    movieId = movieId,
                    currentlyInWatchlist = currentlyInWatchlist
                )
            ) {
                is AppResult.Success -> Unit

                is AppResult.Error -> {
                    _effect.emit(
                        MovieDetailEffect.ShowMessage(result.message)
                    )
                }
            }
        }
    }

    fun clear() {
        observeMovieDetailJob?.cancel()
        observeFavoriteJob?.cancel()
        observeWatchlistJob?.cancel()
        observeCastJob?.cancel()

        observeMovieDetailJob = null
        observeFavoriteJob = null
        observeWatchlistJob = null
        observeCastJob = null

        scope.cancel()
    }
}