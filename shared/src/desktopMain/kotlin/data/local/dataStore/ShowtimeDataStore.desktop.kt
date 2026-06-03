package data.local.datastore

import java.io.File

fun createDataStore() = createDataStore(
    producePath = {
        val appDir = File(
            System.getProperty("user.home"),
            ".showtime"
        )

        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        File(appDir, DATA_STORE_FILE_NAME).absolutePath
    }
)