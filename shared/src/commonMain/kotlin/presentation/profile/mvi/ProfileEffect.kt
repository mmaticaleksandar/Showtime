package presentation.profile.mvi

sealed interface ProfileEffect {
    data object NavigateToAuth : ProfileEffect
}