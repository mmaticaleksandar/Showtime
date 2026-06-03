package data.local.auth

interface TokenStorage {
    var currentToken: String?

    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}