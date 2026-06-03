package presentation.auth.mvi

object AuthReducer {

    fun reduce(
        state: AuthState,
        change: AuthChange
    ): AuthState {
        return when (change) {
            is AuthChange.FullNameChanged -> state.copy(
                fullName = change.value,
                errorMessage = null
            )

            is AuthChange.UsernameChanged -> state.copy(
                username = change.value,
                errorMessage = null
            )

            is AuthChange.PasswordChanged -> state.copy(
                password = change.value,
                errorMessage = null
            )

            AuthChange.ErrorShown -> state.copy(
                errorMessage = null
            )

            AuthChange.LoadingStarted -> state.copy(
                isLoading = true,
                errorMessage = null
            )

            AuthChange.LoadingStopped -> state.copy(
                isLoading = false
            )

            is AuthChange.ErrorChanged -> state.copy(
                isLoading = false,
                errorMessage = change.message
            )

            AuthChange.LoggedIn -> state.copy(
                isLoading = false,
                isLoggedIn = true,
                errorMessage = null
            )

            AuthChange.LoggedOut -> AuthState()

            AuthChange.SessionExpired -> AuthState(
                errorMessage = "Session expired. Please log in again."
            )

            AuthChange.NoTokenFound -> state.copy(
                isLoading = false,
                isLoggedIn = false
            )

            AuthChange.AuthCheckFailedButTokenExists -> state.copy(
                isLoading = false,
                isLoggedIn = true,
                errorMessage = null
            )
        }
    }
}