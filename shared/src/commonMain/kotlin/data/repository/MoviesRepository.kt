package data.repository

import core.result.AppResult
import data.local.movie.MovieDao
import data.mapper.toDomain
import data.mapper.toEntity
import data.remote.movies.MoviesRemoteDataSource
import domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import domain.model.MovieDetail
import data.local.movie.FavoriteMovieEntity
import data.local.movie.WatchlistMovieEntity
import data.mapper.toCastEntity
import data.local.movie.MovieCastEntity
import data.local.movie.MovieDetailEntity
import data.remote.movies.MovieListItemDto
import kotlinx.coroutines.flow.distinctUntilChanged


class MoviesRepository(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val movieDao: MovieDao
) {
    fun observeMovies(query: String): Flow<List<Movie>> {
        return if (query.isBlank()) {
            movieDao.observeMovies()
        } else {
            movieDao.observeMoviesByQuery(query)
        }.map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun syncMovies(
        page: Int = 1,
        pageSize: Int = 20,
        query: String? = null,
        clearBeforeInsert: Boolean = true,
        sortBy: String? = null,
        sortOrder: String? = null,
        minRating: Double? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        genreId: Int? = null
    ): AppResult<Int> {
        return try {
            val response = remoteDataSource.getMovies(
                page = page,
                pageSize = pageSize,
                query = query,
                sortBy = sortBy,
                sortOrder = sortOrder,
                minRating = minRating,
                minYear = minYear,
                maxYear = maxYear,
                genreId = genreId
            )

            val entities = response.items.map { it.toEntity() }

            if (clearBeforeInsert) {
                movieDao.clearMovies()
            }

            movieDao.upsertMovies(entities)

            AppResult.Success(response.totalPages)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to sync movies")
        }
    }

    suspend fun bootstrapQuizPool(): AppResult<Unit> {
        return try {
            val moviesById = linkedMapOf<String, MovieListItemDto>()

            var page = 1
            val pageSize = 20

            while (moviesById.size < 100) {
                val response = remoteDataSource.getMovies(
                    page = page,
                    pageSize = pageSize,
                    query = null,
                    sortBy = "popularity",
                    sortOrder = "desc",
                    minRating = null,
                    minYear = null,
                    maxYear = null,
                    genreId = null
                )

                if (response.items.isEmpty()) {
                    break
                }

                response.items
                    .filter { movie ->
                        !movie.posterPath.isNullOrBlank() ||
                                !movie.backdropPath.isNullOrBlank()
                    }
                    .forEach { movie ->
                        moviesById[movie.imdbId] = movie
                    }

                if (page >= response.totalPages) {
                    break
                }

                page++
            }

            val entities = moviesById
                .values
                .take(100)
                .map { movie ->
                    movie.toEntity()
                }

            movieDao.upsertMovies(entities)

            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(
                message = e.message ?: "Failed to bootstrap quiz pool"
            )
        }
    }

    fun observeMovieDetail(movieId: String): Flow<MovieDetail?> {
        return movieDao
            .observeMovieDetail(movieId)
            .map { entity ->
                entity?.toDomain()
            }
    }

    suspend fun syncMovieDetail(movieId: String): AppResult<Unit> {
        return try {
            val detail = remoteDataSource.getMovieDetails(movieId)
            syncMovieCast(movieId)

            movieDao.upsertMovieDetail(
                detail.toEntity()
            )

            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to sync movie detail")
        }
    }


    //Posmatranje da li je  favorite
    fun observeIsFavorite(movieId: String): Flow<Boolean> {
        return movieDao.observeIsFavorite(movieId)
    }


    //Promena favorite stanja
    suspend fun toggleFavorite(
        movieId: String,
        currentlyFavorite: Boolean
    ): AppResult<Unit> {
        return try {
            if (currentlyFavorite) {
                movieDao.deleteFavorite(movieId)
                remoteDataSource.removeFavorite(movieId)
            } else {
                movieDao.insertFavorite(FavoriteMovieEntity(movieId))
                remoteDataSource.addFavorite(movieId)
            }

            AppResult.Success(Unit)
        } catch (e: Exception) {
            // rollback
            if (currentlyFavorite) {
                movieDao.insertFavorite(FavoriteMovieEntity(movieId))
            } else {
                movieDao.deleteFavorite(movieId)
            }

            AppResult.Error(e.message ?: "Failed to update favorite")
        }
    }

    //Mapiranje favorite filmova
    fun observeFavoriteMovies(): Flow<List<Movie>> {
        return movieDao
            .observeFavoriteMovies()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .distinctUntilChanged()
    }

    //Sync favorites
    suspend fun syncFavoriteMovies(): AppResult<Unit> {
        return try {
            val response = remoteDataSource.getFavorites()

            val movieEntities = response.map { it.toEntity() }

            val favoriteEntities = response.map {
                FavoriteMovieEntity(it.imdbId)
            }

            movieDao.replaceFavoriteMovies(
                movies = movieEntities,
                favorites = favoriteEntities
            )

            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(
                e.message ?: "Failed to sync favorites"
            )
        }
    }


    suspend fun syncUserMovieLists(): AppResult<Unit> {
        return try {
            val favoriteResult = syncFavoriteMovies()
            val watchlistResult = syncWatchlistMovies()

            when {
                favoriteResult is AppResult.Error -> favoriteResult
                watchlistResult is AppResult.Error -> watchlistResult
                else -> AppResult.Success(Unit)
            }
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to sync user movie lists")
        }
    }

    //Watchlist
    //Posmatranje da li je na watchlisti
    fun observeIsInWatchlist(movieId: String): Flow<Boolean> {
        return movieDao.observeIsInWatchlist(movieId)
    }


    //Promena stanja
    suspend fun toggleWatchlist(
        movieId: String,
        currentlyInWatchlist: Boolean
    ): AppResult<Unit> {
        return try {
            if (currentlyInWatchlist) {
                movieDao.deleteWatchlist(movieId)
                remoteDataSource.removeWatchlist(movieId)
            } else {
                movieDao.insertWatchlist(WatchlistMovieEntity(movieId))
                remoteDataSource.addWatchlist(movieId)
            }

            AppResult.Success(Unit)
        } catch (e: Exception) {
            if (currentlyInWatchlist) {
                movieDao.insertWatchlist(WatchlistMovieEntity(movieId))
            } else {
                movieDao.deleteWatchlist(movieId)
            }

            AppResult.Error(e.message ?: "Failed to update watchlist")
        }
    }

    //Mapiranje watch liste
    fun observeWatchlistMovies(): Flow<List<Movie>> {
        return movieDao
            .observeWatchlistMovies()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .distinctUntilChanged()
    }

    //Sync watchlist
    suspend fun syncWatchlistMovies(): AppResult<Unit> {
        return try {
            val response = remoteDataSource.getWatchlist()

            val movieEntities = response.map { it.toEntity() }

            val watchlistEntities = response.map {
                WatchlistMovieEntity(it.imdbId)
            }

            movieDao.replaceWatchlistMovies(
                movies = movieEntities,
                watchlist = watchlistEntities
            )

            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(
                e.message ?: "Failed to sync watchlist"
            )
        }
    }

    //Sync glumci
    suspend fun syncMovieCast(movieId: String): AppResult<Unit> {
        return try {
            val response = remoteDataSource.getMovieCast(movieId)

            val cast = response.items
                .filter {
                    it.department == "Acting"
                }
                .map {
                    it.toCastEntity(movieId)
                }

            movieDao.clearCastForMovie(movieId)
            movieDao.insertCast(cast)

            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to sync movie cast")
        }
    }

    suspend fun ensureMovieCast(movieId: String): AppResult<Unit> {
        val localCast = movieDao.getCastForMovie(movieId)

        if (localCast.isNotEmpty()) {
            return AppResult.Success(Unit)
        }

        return syncMovieCast(movieId)
    }
    fun observeMovieCast(movieId: String): Flow<List<MovieCastEntity>> {
        return movieDao.observeCastForMovie(movieId)
    }

    //Brojanje filmova u listama
    fun observeFavoriteCount(): Flow<Int> {
        return movieDao.observeFavoriteCount()
    }

    fun observeWatchlistCount(): Flow<Int> {
        return movieDao.observeWatchlistCount()
    }
    suspend fun ensureMovieDetail(movieId: String): AppResult<Unit> {
        val localDetail = movieDao.getMovieDetail(movieId)

        if (localDetail?.backdropUrl != null) {
            return AppResult.Success(Unit)
        }

        return syncMovieDetail(movieId)
    }
    suspend fun getLocalMovieDetail(movieId: String): MovieDetailEntity? {
        return movieDao.getMovieDetail(movieId)
    }
}