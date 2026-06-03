package data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val tagline: String?,
    val releaseDate: String?,
    val year: Int?,
    val runtime: Int?,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val tmdbRating: Double?,
    val tmdbVotes: Int?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val genres: String
)