package data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val year: Int?,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val posterUrl: String?,
    val genres: String,
    val backdropUrl: String?
)