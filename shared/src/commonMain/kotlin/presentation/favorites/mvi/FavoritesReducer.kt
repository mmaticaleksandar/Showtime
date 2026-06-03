package presentation.favorites.mvi

object FavoritesReducer {

    fun reduce(
        state: FavoritesState,
        change: FavoritesChange
    ): FavoritesState {
        return when (change) {
            FavoritesChange.LoadingStarted -> state.copy(
                isLoading = state.movies.isEmpty(),
                isOffline = false,
                errorMessage = null
            )

            FavoritesChange.SyncSucceeded -> state.copy(
                isLoading = false,
                isOffline = false,
                errorMessage = null
            )

            is FavoritesChange.FavoritesChanged -> state.copy(
                movies = change.movies,
                isLoading = if (state.isLoading && change.movies.isEmpty()) {
                    true
                } else {
                    false
                },
                errorMessage = null
            )

            is FavoritesChange.LoadFailed -> state.copy(
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