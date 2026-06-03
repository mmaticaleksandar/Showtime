package data.local.auth

import java.io.File

class DesktopTokenStorage : TokenStorage {
    override var currentToken: String? = null
    private val file = File(
        System.getProperty("user.home"),
        ".showtime_token"
    )

    override suspend fun saveToken(token: String) {
        currentToken = token
        file.writeText(token)
    }

    override suspend fun getToken(): String? {
        currentToken = if (file.exists()) {
            file.readText().takeIf { it.isNotBlank() }
        } else {
            null
        }

        return currentToken
    }

    override suspend fun clearToken() {
        currentToken = null
        if (file.exists()) file.delete()
    }
}