package presentation.auth.mvi

sealed interface AuthEffect {
    data object NavigateToMovies : AuthEffect

    data object NavigateToAuth : AuthEffect
    data class ShowMessage(val message: String) : AuthEffect
}