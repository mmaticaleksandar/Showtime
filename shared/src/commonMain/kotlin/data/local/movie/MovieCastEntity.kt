package data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_cast")
data class MovieCastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieId: String,
    val personId: String,
    val name: String,
    val department: String?,
    val profileUrl: String?
)