package core.di

import android.content.Context
import data.local.auth.DataStoreTokenStorage
import data.local.datastore.createDataStore

fun initAndroidAppModule(
    context: Context
) {
    val dataStore = createDataStore(
        context = context.applicationContext
    )

    val tokenStorage = DataStoreTokenStorage(
        dataStore = dataStore
    )

    AppModule.init(
        tokenStorage = tokenStorage
    )
}