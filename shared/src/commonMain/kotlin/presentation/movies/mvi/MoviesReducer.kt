package presentation.movies.mvi

object MoviesReducer {

    fun reduce(
        state: MoviesState,
        change: MoviesChange
    ): MoviesState {
        return when (change) {
            MoviesChange.ReloadStarted -> {
                val hasMovies = state.movies.isNotEmpty()

                state.copy(
                    movies = state.movies,
                    isLoading = !hasMovies,
                    isLoadingMore = false,
                    errorMessage = null,
                    page = 1
                )
            }

            MoviesChange.LoadMoreStarted -> state.copy(
                movies = state.movies,
                isLoading = false,
                isLoadingMore = true,
                errorMessage = null
            )

            is MoviesChange.MoviesChanged -> state.copy(
                movies = change.movies
            )

            is MoviesChange.SyncSucceeded -> state.copy(
                isLoading = false,
                isLoadingMore = false,
                isOffline = false,
                errorMessage = null,
                page = change.page,
                totalPages = change.totalPages
            )

            is MoviesChange.SyncFailed -> state.copy(
                isLoading = false,
                isLoadingMore = false,
                isOffline = change.hasCachedMovies,
                errorMessage = if (change.hasCachedMovies) {
                    null
                } else {
                    change.message
                }
            )

            is MoviesChange.SearchChanged -> state.copy(
                query = change.value,
                errorMessage = null
            )

            MoviesChange.SearchCleared -> state.copy(
                query = "",
                errorMessage = null
            )

            is MoviesChange.SortByChanged -> state.copy(
                sortBy = change.value
            )

            is MoviesChange.SortOrderChanged -> state.copy(
                sortOrder = change.value
            )



            is MoviesChange.MinRatingChanged -> state.copy(
                minRating = change.value
            )

            is MoviesChange.MinYearChanged -> state.copy(
                minYear = change.value
            )

            is MoviesChange.MaxYearChanged -> state.copy(
                maxYear = change.value
            )

            is MoviesChange.GenreChanged -> state.copy(
                selectedGenreId = change.genreId
            )

            MoviesChange.FiltersCleared -> state.copy(
                sortBy = "popularity",
                sortOrder = "desc",
                minRating = "",
                minYear = "",
                maxYear = "",
                selectedGenreId = null,
                page = 1,
                totalPages = null,
                errorMessage = null
            )

            MoviesChange.PrepareForReload -> {
                val hasMovies = state.movies.isNotEmpty()

                state.copy(
                    isLoading = !hasMovies,
                    isLoadingMore = false,
                    errorMessage = null,
                    page = 1
                )
            }
        }
    }
}