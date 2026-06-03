package core.di

import data.local.auth.DataStoreTokenStorage
import data.local.datastore.createDataStore

fun initJvmAppModule() {
    val dataStore = createDataStore()

    val tokenStorage = DataStoreTokenStorage(
        dataStore = dataStore
    )

    AppModule.init(
        tokenStorage = tokenStorage
    )
}