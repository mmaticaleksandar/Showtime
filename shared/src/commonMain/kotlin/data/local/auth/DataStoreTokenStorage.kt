package data.local.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class DataStoreTokenStorage(
    private val dataStore: DataStore<Preferences>
) : TokenStorage {

    override var currentToken: String? = null

    private val tokenKey =
        stringPreferencesKey("access_token")

    override suspend fun saveToken(token: String) {
        currentToken = token

        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    override suspend fun getToken(): String? {

        currentToken =
            dataStore.data.first()[tokenKey]

        return currentToken
    }

    override suspend fun clearToken() {

        currentToken = null

        dataStore.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }
}