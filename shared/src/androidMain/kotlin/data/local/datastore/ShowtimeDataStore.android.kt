package data.local.datastore

import android.content.Context

fun createDataStore(
    context: Context
) = createDataStore(
    producePath = {
        context.filesDir
            .resolve(DATA_STORE_FILE_NAME)
            .absolutePath
    }
)