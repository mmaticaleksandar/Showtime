package presentation.watchlist.mvi

import domain.model.Movie

sealed interface WatchlistChange {

    data object LoadingStarted : WatchlistChange

    data object SyncSucceeded : WatchlistChange

    data class WatchlistChanged(
        val movies: List<Movie>
    ) : WatchlistChange

    data class LoadFailed(
        val message: String,
        val hasCachedMovies: Boolean
    ) : WatchlistChange
}