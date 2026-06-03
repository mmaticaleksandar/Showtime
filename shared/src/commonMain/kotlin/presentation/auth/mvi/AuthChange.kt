package presentation.auth.mvi

sealed interface AuthChange {

    data class FullNameChanged(val value: String) : AuthChange

    data class UsernameChanged(val value: String) : AuthChange

    data class PasswordChanged(val value: String) : AuthChange

    data object ErrorShown : AuthChange

    data object LoadingStarted : AuthChange

    data object LoadingStopped : AuthChange

    data class ErrorChanged(val message: String) : AuthChange

    data object LoggedIn : AuthChange

    data object LoggedOut : AuthChange

    data object SessionExpired : AuthChange

    data object NoTokenFound : AuthChange

    data object AuthCheckFailedButTokenExists : AuthChange
}