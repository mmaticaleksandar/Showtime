package data.local.datastore

import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import java.io.File

fun createDataStore() = createDataStore(
    storage = FileStorage(
        serializer = PreferencesSerializer,
        produceFile = {
            val appDir = File(
                System.getProperty("user.home"),
                ".showtime"
            )

            if (!appDir.exists()) {
                appDir.mkdirs()
            }

            File(appDir, DATA_STORE_FILE_NAME)
        }
    )
)