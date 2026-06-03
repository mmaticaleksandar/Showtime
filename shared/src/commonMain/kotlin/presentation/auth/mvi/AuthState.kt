package presentation.auth.mvi

data class AuthState(
    val fullName: String = "",
    val username: String = "",
    val password: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)