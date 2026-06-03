package data.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

class AndroidDatabaseFactory(
    private val context: Context
) : DatabaseFactory {

    override fun createDatabase(): ShowtimeDatabase {
        val dbFile = context.getDatabasePath("showtime.db")

        return Room.databaseBuilder<ShowtimeDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}