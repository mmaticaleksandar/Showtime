package data.remote.movies

import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponseDto(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<PersonSummaryDto>
)

@Serializable
data class PersonSummaryDto(
    val imdbId: String,
    val name: String,
    val professions: String? = null,
    val department: String? = null,
    val profilePath: String? = null
)