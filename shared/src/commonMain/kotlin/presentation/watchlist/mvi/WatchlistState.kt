package presentation.watchlist.mvi

import domain.model.Movie

data class WatchlistState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isOffline: Boolean = false,
    val errorMessage: String? = null
)