package data.remote.network

import data.local.auth.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        tokenStorage: TokenStorage,
        onUnauthorized: suspend () -> Unit
    ): HttpClient {
        return HttpClient {

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 5000
            }

            install(DefaultRequest) {
                val token = tokenStorage.currentToken

                if (token != null) {
                    header(
                        HttpHeaders.Authorization,
                        "Bearer $token"
                    )
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        val token = tokenStorage.currentToken

                        if (!token.isNullOrBlank()) {
                            tokenStorage.clearToken()
                            onUnauthorized()
                        }
                    }
                }

                handleResponseExceptionWithRequest { exception, _ ->
                    if (
                        exception is ResponseException &&
                        exception.response.status == HttpStatusCode.Unauthorized
                    ) {
                        tokenStorage.clearToken()
                        onUnauthorized()
                    }
                }
            }
        }
    }
}