package presentation.favorites.mvi

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

class FavoritesStore(
    private val moviesRepository: MoviesRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var observeFavoritesJob: Job? = null
    private var syncFavoritesJob: Job? = null

    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FavoritesEffect>()
    val effect: SharedFlow<FavoritesEffect> = _effect.asSharedFlow()

    private fun reduce(change: FavoritesChange) {
        _state.value = FavoritesReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            FavoritesIntent.LoadFavorites -> {
                loadFavorites()
            }

            is FavoritesIntent.RemoveFavorite -> {
                removeFavorite(intent.movieId)
            }
        }
    }

    private fun loadFavorites() {
        reduce(FavoritesChange.LoadingStarted)

        if (observeFavoritesJob == null) {
            observeFavorites()
        }

        syncFavorites()
    }

    private fun observeFavorites() {
        observeFavoritesJob = scope.launch {
            moviesRepository
                .observeFavoriteMovies()
                .collectLatest { movies ->
                    reduce(
                        FavoritesChange.FavoritesChanged(
                            movies = movies
                        )
                    )
                }
        }
    }

    private fun syncFavorites() {
        if (syncFavoritesJob?.isActive == true) {
            return
        }

        syncFavoritesJob = scope.launch {
            when (val result = moviesRepository.syncFavoriteMovies()) {
                is AppResult.Success -> {
                    reduce(FavoritesChange.SyncSucceeded)
                }

                is AppResult.Error -> {
                    val hasCachedMovies = _state.value.movies.isNotEmpty()

                    reduce(
                        FavoritesChange.LoadFailed(
                            message = result.message,
                            hasCachedMovies = hasCachedMovies
                        )
                    )

                    if (hasCachedMovies) {
                        _effect.emit(
                            FavoritesEffect.ShowMessage(result.message)
                        )
                    }
                }
            }
        }
    }

    private fun removeFavorite(movieId: String) {
        scope.launch {
            when (
                val result = moviesRepository.toggleFavorite(
                    movieId = movieId,
                    currentlyFavorite = true
                )
            ) {
                is AppResult.Success -> Unit

                is AppResult.Error -> {
                    _effect.emit(
                        FavoritesEffect.ShowMessage(result.message)
                    )
                }
            }
        }
    }

    fun clear() {
        observeFavoritesJob?.cancel()
        observeFavoritesJob = null

        syncFavoritesJob?.cancel()
        syncFavoritesJob = null

        scope.cancel()
    }
}