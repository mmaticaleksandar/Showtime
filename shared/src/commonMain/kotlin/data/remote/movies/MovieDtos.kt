package data.remote.movies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponseDto(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<MovieListItemDto>
)

@Serializable
data class MovieListItemDto(
    val imdbId: String,
    val title: String,
    val year: Int? = null,
    val imdbRating: Double? = null,
    val imdbVotes: Int? = null,
    val posterPath: String? = null,
    val backdropPath : String? = null,
    val genres: List<GenreDto> = emptyList()
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)