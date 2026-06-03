package data.local.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.authDataStore by preferencesDataStore(name = "auth")

class AndroidTokenStorage(
    private val context: Context
) : TokenStorage {
    override var currentToken: String? = null
    private val tokenKey = stringPreferencesKey("token")

    override suspend fun saveToken(token: String) {
        context.authDataStore.edit { preferences ->
            currentToken = token
            preferences[tokenKey] = token

        }
    }

    override suspend fun getToken(): String? {
        val preferences = context.authDataStore.data.first()
        currentToken = preferences[tokenKey]
        return currentToken

    }

    override suspend fun clearToken() {
        context.authDataStore.edit { preferences ->
            currentToken = null
            preferences.remove(tokenKey)
        }
    }
}