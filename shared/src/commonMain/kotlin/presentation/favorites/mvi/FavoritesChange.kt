package presentation.favorites.mvi

import domain.model.Movie

sealed interface FavoritesChange {

    data object LoadingStarted : FavoritesChange

    data object SyncSucceeded : FavoritesChange

    data class FavoritesChanged(
        val movies: List<Movie>
    ) : FavoritesChange

    data class LoadFailed(
        val message: String,
        val hasCachedMovies: Boolean
    ) : FavoritesChange
}