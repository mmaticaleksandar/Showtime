package presentation.movieDetail.mvi

sealed interface MovieDetailIntent {
    data object ToggleFavorite : MovieDetailIntent

    data object ToggleWatchlist : MovieDetailIntent
    data class LoadMovieDetail(val movieId: String) : MovieDetailIntent

}