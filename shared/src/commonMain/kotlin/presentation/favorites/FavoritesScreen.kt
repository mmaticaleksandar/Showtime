package presentation.favorites

import androidx.compose.runtime.Composable
import presentation.common.SavedMoviesScreenContent
import presentation.favorites.mvi.FavoritesState

@Composable
fun FavoritesScreen(
    state: FavoritesState,
    onBackClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit
) {
    SavedMoviesScreenContent(
        title = "Favorites",
        emptyMessage = "No favorite movies yet.",
        offlineMessage = "Offline mode: showing saved favorite movies.",
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        isOffline = state.isOffline,
        movies = state.movies,
        onBackClick = onBackClick,
        onMovieClick = onMovieClick,
        onRemoveClick = onRemoveClick
    )
}