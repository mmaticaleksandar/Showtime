package data.local.auth

import data.local.datastore.DATA_STORE_FILE_NAME
import data.local.datastore.createDataStore
import java.io.File

fun createJvmTokenStorage(): TokenStorage {
    val dataStoreFile = File(
        System.getProperty("user.home"),
        ".showtime/$DATA_STORE_FILE_NAME"
    )

    dataStoreFile.parentFile?.mkdirs()

    return DataStoreTokenStorage(
        dataStore = createDataStore(
            producePath = {
                dataStoreFile.absolutePath
            }
        )
    )
}