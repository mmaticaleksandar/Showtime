package presentation.profile.mvi

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent

    data object LogoutClicked : ProfileIntent
}