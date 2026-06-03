package presentation.watchlist.mvi

sealed interface WatchlistEffect {
    data class ShowMessage(
        val message: String
    ) : WatchlistEffect
}