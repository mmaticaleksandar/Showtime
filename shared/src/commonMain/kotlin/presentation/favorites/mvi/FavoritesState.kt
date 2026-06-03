package presentation.favorites.mvi

import domain.model.Movie

data class FavoritesState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOffline: Boolean = false,
)