package presentation.auth.mvi

sealed interface AuthIntent {
    data object CheckAuthStatus : AuthIntent


    data class FullNameChanged(val value: String) : AuthIntent
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent


    data object LoginClicked : AuthIntent
    data object RegisterClicked : AuthIntent
    data object ErrorShown : AuthIntent

    data object LogoutClicked : AuthIntent

    data object ForceLogout : AuthIntent

}