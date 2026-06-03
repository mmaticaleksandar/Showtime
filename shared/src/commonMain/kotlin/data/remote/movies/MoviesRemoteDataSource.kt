package data.remote.movies

import data.remote.network.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.delete
import io.ktor.client.request.post

class MoviesRemoteDataSource(
    private val client: HttpClient
) {

    //Svi filmovi
    suspend fun getMovies(
        page: Int = 1,
        pageSize: Int = 20,
        query: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        minRating: Double? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        genreId: Int? = null
    ): MoviesResponseDto {
        return client.get("${ApiConfig.BASE_URL}/movies") {
            parameter("page", page)
            parameter("page_size", pageSize)
            sortBy?.let {
                parameter("sort_by", it)
            }

            sortOrder?.let {
                parameter("sort_order", it)
            }

            minRating?.let {
                parameter("min_rating", it)
            }

            minYear?.let {
                parameter("min_year", it)
            }

            maxYear?.let {
                parameter("max_year", it)
            }

            genreId?.let {
                parameter("genre_id", it)
            }

            if (!query.isNullOrBlank()) {
                parameter("query", query)
            }
        }.body()
    }

    //Detalji filma
    suspend fun getMovieDetails(
        movieId: String
    ): MovieDetailDto {
        return client.get("${ApiConfig.BASE_URL}/movies/$movieId")
            .body()
    }

    //Favorites
    suspend fun addFavorite(movieId: String) {
        client.post("${ApiConfig.BASE_URL}/me/favorites/$movieId")
    }

    suspend fun removeFavorite(movieId: String) {
        client.delete("${ApiConfig.BASE_URL}/me/favorites/$movieId")
    }


    //Watchlista
    suspend fun addWatchlist(movieId: String) {
        client.post("${ApiConfig.BASE_URL}/me/watchlist/$movieId")
    }

    suspend fun removeWatchlist(movieId: String) {
        client.delete("${ApiConfig.BASE_URL}/me/watchlist/$movieId")
    }

    //Get za favorties i watchlist
    suspend fun getFavorites(): List<MovieListItemDto> {
        return client.get("${ApiConfig.BASE_URL}/me/favorites")
            .body()
    }

    suspend fun getWatchlist(): List<MovieListItemDto> {
        return client.get("${ApiConfig.BASE_URL}/me/watchlist")
            .body()
    }

    suspend fun getMovieCast(
        movieId: String,
        pageSize: Int = 10
    ): MovieCastResponseDto {
        return client.get("${ApiConfig.BASE_URL}/movies/$movieId/cast") {
            parameter("page_size", pageSize)
        }.body()
    }
}