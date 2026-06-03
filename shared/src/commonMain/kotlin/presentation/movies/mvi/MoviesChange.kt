package presentation.movies.mvi

import domain.model.Movie

sealed interface MoviesChange {

    data object ReloadStarted : MoviesChange

    data object LoadMoreStarted : MoviesChange

    data class MoviesChanged(
        val movies: List<Movie>
    ) : MoviesChange

    data class SyncSucceeded(
        val page: Int,
        val totalPages: Int?
    ) : MoviesChange

    data class SyncFailed(
        val message: String,
        val hasCachedMovies: Boolean
    ) : MoviesChange

    data class SearchChanged(
        val value: String
    ) : MoviesChange

    data object SearchCleared : MoviesChange

    data class SortByChanged(
        val value: String
    ) : MoviesChange

    data class SortOrderChanged(
        val value: String
    ) : MoviesChange



    data class MinRatingChanged(
        val value: String
    ) : MoviesChange

    data class MinYearChanged(
        val value: String
    ) : MoviesChange

    data class MaxYearChanged(
        val value: String
    ) : MoviesChange

    data class GenreChanged(
        val genreId: Int?
    ) : MoviesChange

    data object FiltersCleared : MoviesChange

    data object PrepareForReload : MoviesChange
}