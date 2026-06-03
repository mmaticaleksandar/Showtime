package presentation.movieDetail.mvi

sealed interface MovieDetailEffect {
    data class ShowMessage(
        val message: String
    ) : MovieDetailEffect
}