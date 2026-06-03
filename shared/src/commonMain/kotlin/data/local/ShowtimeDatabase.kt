package data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import data.local.movie.MovieDao
import data.local.movie.MovieEntity
import data.local.movie.MovieDetailEntity
import data.local.movie.FavoriteMovieEntity
import data.local.movie.WatchlistMovieEntity
import data.local.quiz.QuizDao
import data.local.quiz.QuizStatsEntity
import data.local.movie.MovieCastEntity

@Database(
    entities = [
        MovieEntity::class,
        MovieDetailEntity::class,
        FavoriteMovieEntity::class,
        WatchlistMovieEntity::class,
        QuizStatsEntity::class,
        MovieCastEntity::class
    ],
    version = 8
)
abstract class ShowtimeDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun quizDao(): QuizDao
}