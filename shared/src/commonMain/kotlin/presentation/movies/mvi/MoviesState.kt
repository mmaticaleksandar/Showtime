package presentation.movies.mvi

import domain.model.Movie

data class MoviesState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOffline: Boolean = false,
    val isLoadingMore: Boolean = false,

    //pretraga
    val query: String = "",
    val page: Int = 1,
    val totalPages: Int? = null,

    //sort
    val sortBy: String = "popularity",
    val sortOrder: String = "desc",


    //Filteri
    val minRating: String = "",
    val minYear: String = "",
    val maxYear: String = "",
    val selectedGenreId: Int? = null
)