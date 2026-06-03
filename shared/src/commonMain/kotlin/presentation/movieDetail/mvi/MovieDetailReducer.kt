package presentation.movieDetail.mvi

object MovieDetailReducer {

    fun reduce(
        state: MovieDetailState,
        change: MovieDetailChange
    ): MovieDetailState {
        return when (change) {
            MovieDetailChange.LoadingStarted -> MovieDetailState(
                movie = state.movie,
                cast = state.cast,
                isFavorite = state.isFavorite,
                isInWatchlist = state.isInWatchlist,
                isLoading = true,
                isOffline = false,
                errorMessage = null
            )

            MovieDetailChange.DetailSyncSucceeded -> state.copy(
                isLoading = false,
                isOffline = false,
                errorMessage = null
            )

            is MovieDetailChange.DetailLoadFailed -> state.copy(
                isLoading = false,
                isOffline = change.hasCachedMovie,
                errorMessage = if (change.hasCachedMovie) {
                    null
                } else {
                    change.message
                }
            )

            is MovieDetailChange.MovieChanged -> state.copy(
                movie = change.movie
            )

            is MovieDetailChange.CastChanged -> state.copy(
                cast = change.cast
            )

            is MovieDetailChange.FavoriteChanged -> state.copy(
                isFavorite = change.isFavorite
            )

            is MovieDetailChange.WatchlistChanged -> state.copy(
                isInWatchlist = change.isInWatchlist
            )
        }
    }
}