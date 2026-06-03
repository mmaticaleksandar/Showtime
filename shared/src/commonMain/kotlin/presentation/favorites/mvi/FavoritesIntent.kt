package presentation.favorites.mvi

sealed interface FavoritesIntent {
    data object LoadFavorites : FavoritesIntent

    data class RemoveFavorite(val movieId: String) : FavoritesIntent
}