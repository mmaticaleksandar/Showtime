package presentation.movies.mvi

import core.result.AppResult
import data.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesStore(
    private val moviesRepository: MoviesRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var observeMoviesJob: Job? = null

    private val _state = MutableStateFlow(MoviesState())
    val state: StateFlow<MoviesState> = _state.asStateFlow()

    private fun reduce(change: MoviesChange) {
        _state.value = MoviesReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    fun onIntent(intent: MoviesIntent) {
        when (intent) {
            MoviesIntent.LoadMovies -> {
                reloadMovies()
            }

            is MoviesIntent.SearchChanged -> {
                reduce(
                    MoviesChange.SearchChanged(
                        value = intent.value
                    )
                )
            }

            MoviesIntent.SearchSubmitted -> {
                reloadMovies()
            }

            MoviesIntent.ClearSearch -> {
                reduce(MoviesChange.SearchCleared)
                reloadMovies()
            }

            MoviesIntent.LoadNextPage -> {
                loadNextPage()
            }

            is MoviesIntent.SortByChanged -> {
                reduce(
                    MoviesChange.SortByChanged(
                        value = intent.value
                    )
                )
            }

            is MoviesIntent.SortOrderChanged -> {
                reduce(
                    MoviesChange.SortOrderChanged(
                        value = intent.value
                    )
                )
            }

            MoviesIntent.ApplySorting -> {
                reloadMovies()
            }



            is MoviesIntent.MinRatingChanged -> {
                reduce(
                    MoviesChange.MinRatingChanged(
                        value = intent.value
                    )
                )
            }

            is MoviesIntent.MinYearChanged -> {
                reduce(
                    MoviesChange.MinYearChanged(
                        value = intent.value
                    )
                )
            }

            is MoviesIntent.MaxYearChanged -> {
                reduce(
                    MoviesChange.MaxYearChanged(
                        value = intent.value
                    )
                )
            }

            is MoviesIntent.GenreChanged -> {
                reduce(
                    MoviesChange.GenreChanged(
                        genreId = intent.genreId
                    )
                )
            }

            MoviesIntent.ApplyFilters -> {
                reloadMovies()
            }

            MoviesIntent.ClearFilters -> {
                reduce(MoviesChange.FiltersCleared)
                reloadMovies()
            }

            MoviesIntent.PrepareForReload -> {
                reduce(MoviesChange.PrepareForReload)
            }

            MoviesIntent.StopObservingMovies -> {
                observeMoviesJob?.cancel()
                observeMoviesJob = null
            }

        }
    }

    private fun reloadMovies() {
        observeMoviesJob?.cancel()
        observeMoviesJob = null

        reduce(MoviesChange.ReloadStarted)

        syncMovies(
            reset = true,
            observeAfterSync = true
        )
    }

    private fun loadNextPage() {
        val state = _state.value

        if (state.isLoading || state.isLoadingMore) return

        val totalPages = state.totalPages
        if (totalPages != null && state.page >= totalPages) return

        syncMovies(reset = false)
    }

    private fun observeMovies() {
        observeMoviesJob?.cancel()

        val query = _state.value.query

        observeMoviesJob = scope.launch {
            moviesRepository
                .observeMovies(query)
                .collectLatest { movies ->
                    reduce(
                        MoviesChange.MoviesChanged(
                            movies = movies
                        )
                    )
                }
        }
    }

    private fun syncMovies(
        reset: Boolean,
        observeAfterSync: Boolean = false
    ) {
        val currentState = _state.value

        if (!reset) {
            reduce(MoviesChange.LoadMoreStarted)
        }

        scope.launch {
            val pageToLoad = if (reset) {
                1
            } else {
                currentState.page + 1
            }

            when (
                val result = moviesRepository.syncMovies(
                    page = pageToLoad,
                    pageSize = 20,
                    query = currentState.query.takeIf { it.isNotBlank() },
                    sortBy = currentState.sortBy,
                    sortOrder = currentState.sortOrder,
                    minRating = currentState.minRating.toDoubleOrNull(),
                    minYear = currentState.minYear.toIntOrNull(),
                    maxYear = currentState.maxYear.toIntOrNull(),
                    genreId = currentState.selectedGenreId,
                    clearBeforeInsert = reset
                )
            ) {
                is AppResult.Success -> {
                    reduce(
                        MoviesChange.SyncSucceeded(
                            page = pageToLoad,
                            totalPages = result.data
                        )
                    )

                    if (observeAfterSync) {
                        observeMovies()
                    }
                }

                is AppResult.Error -> {
                    if (observeAfterSync) {
                        observeMovies()
                    }

                    val hasCachedMovies = _state.value.movies.isNotEmpty()

                    reduce(
                        MoviesChange.SyncFailed(
                            message = result.message,
                            hasCachedMovies = hasCachedMovies
                        )
                    )
                }
            }
        }
    }

    fun clear() {
        observeMoviesJob?.cancel()
        observeMoviesJob = null

        scope.cancel()
    }
}