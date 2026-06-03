package presentation.favorites.mvi

sealed interface FavoritesEffect {
    data class ShowMessage(
        val message: String
    ) : FavoritesEffect
}