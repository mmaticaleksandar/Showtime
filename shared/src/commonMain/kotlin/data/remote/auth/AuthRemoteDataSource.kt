package data.remote.auth

import data.remote.network.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.request.get

class AuthRemoteDataSource(
    private val client: HttpClient
) {
    suspend fun login(
        username: String,
        password: String
    ): AuthResponse {
        return client.post("${ApiConfig.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                LoginRequest(
                    username = username,
                    password = password
                )
            )
        }.body()
    }

    suspend fun register(
        fullName: String,
        username: String,
        password: String
    ): AuthResponse {
        return client.post("${ApiConfig.BASE_URL}/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(
                RegisterRequest(
                    fullName = fullName,
                    username = username,
                    password = password
                )
            )
        }.body()
    }

    // Dohvatanje userovih podataka
    suspend fun getMe(): UserDto {
        return client.get("${ApiConfig.BASE_URL}/me")
            .body()
    }
}