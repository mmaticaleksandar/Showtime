package presentation.movieDetail.mvi

import domain.model.MovieDetail

sealed interface MovieDetailChange {

    data object LoadingStarted : MovieDetailChange

    data object DetailSyncSucceeded : MovieDetailChange

    data class DetailLoadFailed(
        val message: String,
        val hasCachedMovie: Boolean
    ) : MovieDetailChange

    data class MovieChanged(
        val movie: MovieDetail?
    ) : MovieDetailChange

    data class CastChanged(
        val cast: List<String>
    ) : MovieDetailChange

    data class FavoriteChanged(
        val isFavorite: Boolean
    ) : MovieDetailChange

    data class WatchlistChanged(
        val isInWatchlist: Boolean
    ) : MovieDetailChange
}