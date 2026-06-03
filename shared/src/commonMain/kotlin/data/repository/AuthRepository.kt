package data.repository

import data.local.auth.TokenStorage
import data.remote.auth.AuthRemoteDataSource
import data.remote.auth.AuthResponse
import data.remote.auth.UserDto
import core.result.AppResult
import data.local.movie.MovieDao

class AuthRepository(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenStorage: TokenStorage,
    private val movieDao: MovieDao
) {
    private var currentUser: UserDto? = null

    fun getCurrentUserId(): Int? {
        return currentUser?.id
    }
    suspend fun login(
        username: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            val response = remoteDataSource.login(
                username = username,
                password = password
            )

            tokenStorage.saveToken(response.accessToken)
            currentUser = response.user

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        fullName: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            val response = remoteDataSource.register(
                fullName = fullName,
                username = username,
                password = password
            )

            tokenStorage.saveToken(response.accessToken)
            currentUser = response.user

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSavedToken(): String? {
        return tokenStorage.getToken()
    }
    suspend fun getToken(): String? {
        return tokenStorage.getToken()
    }

    suspend fun logout() {
        currentUser = null
        tokenStorage.clearToken()
        movieDao.clearFavorites()
        movieDao.clearWatchlist()
    }

    //Ucitavanje profila
    suspend fun getMe(): AppResult<UserDto> {
        return try {
            val user = remoteDataSource.getMe()
            currentUser = user
            AppResult.Success(user)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to load profile")
        }
    }
}