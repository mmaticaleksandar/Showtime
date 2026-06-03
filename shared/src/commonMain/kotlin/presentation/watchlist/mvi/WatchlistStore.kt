package presentation.watchlist.mvi

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

class WatchlistStore(
    private val moviesRepository: MoviesRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var observeWatchlistJob: Job? = null
    private var syncWatchlistJob: Job? = null

    private val _state = MutableStateFlow(WatchlistState())
    val state: StateFlow<WatchlistState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WatchlistEffect>()
    val effect: SharedFlow<WatchlistEffect> = _effect.asSharedFlow()

    private fun reduce(change: WatchlistChange) {
        _state.value = WatchlistReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    fun onIntent(intent: WatchlistIntent) {
        when (intent) {
            WatchlistIntent.LoadWatchlist -> {
                loadWatchlist()
            }

            is WatchlistIntent.RemoveWatchlist -> {
                removeWatchlist(intent.movieId)
            }
        }
    }

    private fun loadWatchlist() {
        reduce(WatchlistChange.LoadingStarted)

        if (observeWatchlistJob == null) {
            observeWatchlist()
        }

        syncWatchlist()
    }

    private fun observeWatchlist() {
        observeWatchlistJob = scope.launch {
            moviesRepository
                .observeWatchlistMovies()
                .collectLatest { movies ->
                    reduce(
                        WatchlistChange.WatchlistChanged(
                            movies = movies
                        )
                    )
                }
        }
    }

    private fun syncWatchlist() {
        if (syncWatchlistJob?.isActive == true) {
            return
        }

        syncWatchlistJob = scope.launch {
            when (val result = moviesRepository.syncWatchlistMovies()) {
                is AppResult.Success -> {
                    reduce(WatchlistChange.SyncSucceeded)
                }

                is AppResult.Error -> {
                    val hasCachedMovies = _state.value.movies.isNotEmpty()

                    reduce(
                        WatchlistChange.LoadFailed(
                            message = result.message,
                            hasCachedMovies = hasCachedMovies
                        )
                    )

                    if (hasCachedMovies) {
                        _effect.emit(
                            WatchlistEffect.ShowMessage(result.message)
                        )
                    }
                }
            }
        }
    }

    private fun removeWatchlist(movieId: String) {
        scope.launch {
            when (
                val result = moviesRepository.toggleWatchlist(
                    movieId = movieId,
                    currentlyInWatchlist = true
                )
            ) {
                is AppResult.Success -> Unit

                is AppResult.Error -> {
                    _effect.emit(
                        WatchlistEffect.ShowMessage(result.message)
                    )
                }
            }
        }
    }

    fun clear() {
        observeWatchlistJob?.cancel()
        observeWatchlistJob = null

        syncWatchlistJob?.cancel()
        syncWatchlistJob = null

        scope.cancel()
    }
}