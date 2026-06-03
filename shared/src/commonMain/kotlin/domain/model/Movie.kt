package domain.model

data class Movie(
    val imdbId: String,
    val title: String,
    val year: Int?,
    val imdbRating: Double?,
    val imdbVotes: Int?,
    val posterUrl: String?,
    val genres: List<String>,
    val backdropUrl: String?
)