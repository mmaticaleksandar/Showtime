package presentation.movieDetail.mvi

import domain.model.MovieDetail

data class MovieDetailState(
    val movie: MovieDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOffline: Boolean = false,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val cast: List<String> = emptyList()
)