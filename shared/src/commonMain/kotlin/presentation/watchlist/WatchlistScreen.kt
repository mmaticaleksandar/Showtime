package presentation.watchlist

import androidx.compose.runtime.Composable
import presentation.common.SavedMoviesScreenContent
import presentation.watchlist.mvi.WatchlistState

@Composable
fun WatchlistScreen(
    state: WatchlistState,
    onBackClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit
) {
    SavedMoviesScreenContent(
        title = "Watchlist",
        emptyMessage = "No movies in watchlist.",
        offlineMessage = "Offline mode: showing saved watchlist movies.",
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        isOffline = state.isOffline,
        movies = state.movies,
        onBackClick = onBackClick,
        onMovieClick = onMovieClick,
        onRemoveClick = onRemoveClick
    )
}