package data.local.auth

class InMemoryTokenStorage : TokenStorage {

    override var currentToken: String? = null

    override suspend fun saveToken(token: String) {
        currentToken = token
    }

    override suspend fun getToken(): String? {
        return currentToken
    }

    override suspend fun clearToken() {
        currentToken = null
    }
}