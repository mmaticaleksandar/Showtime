package presentation.movies.mvi

sealed interface MoviesIntent {
    data object LoadMovies : MoviesIntent
    data class SearchChanged(val value: String) : MoviesIntent
    data object SearchSubmitted : MoviesIntent

    data object ClearSearch : MoviesIntent
    data object ClearAll : MoviesIntent
    data object LoadNextPage : MoviesIntent

    data class SortByChanged(val value: String) : MoviesIntent
    data class SortOrderChanged(val value: String) : MoviesIntent
    data object ApplySorting : MoviesIntent

    data class MinRatingChanged(val value: String) : MoviesIntent

    data class MinYearChanged(val value: String) : MoviesIntent
    data class MaxYearChanged(val value: String) : MoviesIntent

    data class GenreChanged(val genreId: Int?) : MoviesIntent

    data object ApplyFilters : MoviesIntent
    data object ClearFilters : MoviesIntent

    data object PrepareForReload : MoviesIntent

    data object StopObservingMovies : MoviesIntent
}