package data.mapper

import data.local.movie.MovieEntity
import data.remote.movies.MovieListItemDto
import data.remote.network.ImageUrlBuilder
import domain.model.Movie
import data.local.movie.MovieDetailEntity
import data.remote.movies.MovieDetailDto
import domain.model.MovieDetail
import data.local.movie.MovieCastEntity
import data.remote.movies.PersonSummaryDto

fun MovieListItemDto.toEntity(): MovieEntity {
    return MovieEntity(
        imdbId = imdbId,
        title = title,
        year = year,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        posterUrl = ImageUrlBuilder.posterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.backdropUrl(backdropPath),
        genres = genres.joinToString(", ") { it.name }
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        imdbId = imdbId,
        title = title,
        year = year,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        posterUrl = posterUrl,
        backdropUrl =backdropUrl,
        genres = genres
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    )
}

fun MovieDetailDto.toEntity(): MovieDetailEntity {
    return MovieDetailEntity(
        imdbId = imdbId,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        tagline = tagline,
        releaseDate = releaseDate,
        year = year,
        runtime = runtime,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        tmdbRating = tmdbRating,
        tmdbVotes = tmdbVotes,
        posterUrl = ImageUrlBuilder.posterUrl(posterPath),
        backdropUrl = ImageUrlBuilder.backdropUrl(backdropPath),
        genres = genres.joinToString(", ") { it.name }
    )
}

fun MovieDetailEntity.toDomain(): MovieDetail {
    return MovieDetail(
        imdbId = imdbId,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        tagline = tagline,
        releaseDate = releaseDate,
        year = year,
        runtime = runtime,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        tmdbRating = tmdbRating,
        tmdbVotes = tmdbVotes,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        genres = genres
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    )
}

fun PersonSummaryDto.toCastEntity(movieId: String): MovieCastEntity {
    return MovieCastEntity(
        movieId = movieId,
        personId = imdbId,
        name = name,
        department = department,
        profileUrl = ImageUrlBuilder.profileUrl(profilePath)
    )
}