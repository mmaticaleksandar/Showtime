package data.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

class DesktopDatabaseFactory : DatabaseFactory {

    override fun createDatabase(): ShowtimeDatabase {
        val dbFile = File(
            System.getProperty("user.home"),
            "showtime.db"
        )

        return Room.databaseBuilder<ShowtimeDatabase>(
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}