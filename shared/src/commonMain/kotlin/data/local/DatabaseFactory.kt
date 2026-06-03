package data.local

interface DatabaseFactory {
    fun createDatabase(): ShowtimeDatabase
}