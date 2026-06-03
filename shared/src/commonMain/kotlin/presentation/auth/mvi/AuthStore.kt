package presentation.auth.mvi

import core.result.AppResult
import data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.io.IOException
import data.repository.MoviesRepository

class AuthStore(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository
) {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    private fun reduce(change: AuthChange) {
        _state.value = AuthReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    suspend fun onIntent(intent: AuthIntent) {
        when (intent) {
            AuthIntent.CheckAuthStatus -> checkAuthStatus()


            is AuthIntent.FullNameChanged -> {
                reduce(AuthChange.FullNameChanged(intent.value))
            }

            is AuthIntent.UsernameChanged -> {
                reduce(AuthChange.UsernameChanged(intent.value))
            }

            is AuthIntent.PasswordChanged -> {
                reduce(AuthChange.PasswordChanged(intent.value))
            }

            AuthIntent.LoginClicked -> login()

            AuthIntent.RegisterClicked -> register()
            AuthIntent.LogoutClicked -> logout()
            AuthIntent.ErrorShown -> {
                reduce(AuthChange.ErrorShown)
            }

            AuthIntent.ForceLogout -> forceLogout()
        }
    }

    private suspend fun forceLogout() {
        authRepository.logout()

        reduce(AuthChange.SessionExpired)

        _effect.emit(AuthEffect.NavigateToAuth)
    }

    private suspend fun logout() {
        authRepository.logout()

        reduce(AuthChange.LoggedOut)

        _effect.emit(AuthEffect.NavigateToAuth)
    }

    private suspend fun login() {
        val currentState = _state.value

        if (currentState.username.isBlank() && currentState.password.isNotBlank()) {
            reduce(AuthChange.ErrorChanged("Username is required."))
            return
        }
        if (currentState.username.isNotBlank() && currentState.password.isBlank()) {
            reduce(AuthChange.ErrorChanged("Password is required."))
            return
        }
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            reduce(AuthChange.ErrorChanged("Username and password are required."))
            return
        }


        reduce(AuthChange.LoadingStarted)

        val result = authRepository.login(
            username = currentState.username,
            password = currentState.password
        )

        result
            .onSuccess {
                syncUserMovieListsAfterAuth()

                reduce(AuthChange.LoggedIn)

                _effect.emit(AuthEffect.NavigateToMovies)
            }
            .onFailure { error ->
                reduce(AuthChange.ErrorChanged(loginErrorMessage(error)))
            }
    }

    private suspend fun register() {
        val currentState = _state.value

        if (
            currentState.fullName.isBlank() ||
            currentState.username.isBlank() ||
            currentState.password.isBlank()
        ) {
            reduce(AuthChange.ErrorChanged("All fields are required."))
            return
        }

        if (!currentState.username.matches(Regex("^[a-zA-Z0-9_]{3,}$"))) {
            reduce(
                AuthChange.ErrorChanged(
                    "Username must have at least 3 characters and contain only letters, numbers and underscore."
                )
            )
            return
        }

        if (currentState.password.length < 8) {
            reduce(AuthChange.ErrorChanged("Password must have at least 8 characters."))
            return
        }

        reduce(AuthChange.LoadingStarted)

        val result = authRepository.register(
            fullName = currentState.fullName,
            username = currentState.username,
            password = currentState.password
        )

        result
            .onSuccess {
                syncUserMovieListsAfterAuth()
                reduce(AuthChange.LoggedIn)

                _effect.emit(AuthEffect.NavigateToMovies)
            }
            .onFailure { error ->
                reduce(AuthChange.ErrorChanged(registerErrorMessage(error)))
            }
    }
    private suspend fun checkAuthStatus() {
        reduce(AuthChange.LoadingStarted)

        val token = authRepository.getToken()

        if (token.isNullOrBlank()) {
            reduce(AuthChange.NoTokenFound)

            _effect.emit(AuthEffect.NavigateToAuth)
            return
        }

        when (val result = authRepository.getMe()) {
            is AppResult.Success -> {
                syncUserMovieListsAfterAuth()
                reduce(AuthChange.LoggedIn)

                _effect.emit(AuthEffect.NavigateToMovies)
            }

            is AppResult.Error -> {
                authRepository.logout()
                reduce(AuthChange.NoTokenFound)
                _effect.emit(AuthEffect.NavigateToAuth)
            }
        }
    }
    private fun loginErrorMessage(error: Throwable): String {
        return when (error) {
            is ClientRequestException -> {
                when (error.response.status.value) {
                    400, 401 -> "Invalid username or password."
                    else -> "Login failed. Please try again."
                }
            }

            is ServerResponseException -> {
                "Server error. Please try again later."
            }

            is ConnectTimeoutException,
            is SocketTimeoutException,
            is IOException -> {
                "Network error. Check your connection."
            }

            else -> {
                "Login failed. Please try again."
            }
        }
    }

    private fun registerErrorMessage(error: Throwable): String {
        return when (error) {
            is ClientRequestException -> {
                when (error.response.status.value) {
                    400, 409 -> "Username is already taken or input is invalid."
                    else -> "Registration failed. Please try again."
                }
            }

            is ServerResponseException -> {
                "Server error. Please try again later."
            }

            is ConnectTimeoutException,
            is SocketTimeoutException,
            is IOException -> {
                "Network error. Check your connection."
            }

            else -> {
                "Registration failed. Please try again."
            }
        }
    }
    private suspend fun syncUserMovieListsAfterAuth() {
        moviesRepository.syncUserMovieLists()
    }
}