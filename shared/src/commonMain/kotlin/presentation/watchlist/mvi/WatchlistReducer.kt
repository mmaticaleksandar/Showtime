package presentation.watchlist.mvi

object WatchlistReducer {

    fun reduce(
        state: WatchlistState,
        change: WatchlistChange
    ): WatchlistState {
        return when (change) {
            WatchlistChange.LoadingStarted -> state.copy(
                isLoading = state.movies.isEmpty(),
                isOffline = false,
                errorMessage = null
            )

            WatchlistChange.SyncSucceeded -> state.copy(
                isLoading = false,
                isOffline = false,
                errorMessage = null
            )

            is WatchlistChange.WatchlistChanged -> state.copy(
                movies = change.movies,
                isLoading = if (state.isLoading && change.movies.isEmpty()) {
                    true
                } else {
                    false
                },
                errorMessage = null
            )

            is WatchlistChange.LoadFailed -> state.copy(
                isLoading = false,
                isOffline = change.hasCachedMovies,
                errorMessage = if (change.hasCachedMovies) {
                    null
                } else {
                    change.message
                }
            )
        }
    }
}