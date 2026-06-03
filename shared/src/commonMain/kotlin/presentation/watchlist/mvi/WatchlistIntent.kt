package presentation.watchlist.mvi

sealed interface WatchlistIntent {
    data object LoadWatchlist : WatchlistIntent
    data class RemoveWatchlist(val movieId: String) : WatchlistIntent
}