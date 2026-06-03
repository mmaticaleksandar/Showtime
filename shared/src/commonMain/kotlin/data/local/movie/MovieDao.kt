package data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

@Dao
interface MovieDao {

    //Svi filmovi
    @Query("SELECT * FROM movies")
    fun observeMovies(): Flow<List<MovieEntity>>

    @Query(
        """
        SELECT * FROM movies
        WHERE title LIKE '%' || :query || '%'
        """
    )

    //Pretraga
    fun observeMoviesByQuery(query: String): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearMovies()


    //Detalji
    @Query("SELECT * FROM movie_details WHERE imdbId = :movieId LIMIT 1")
    fun observeMovieDetail(movieId: String): Flow<MovieDetailEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovieDetail(movieDetail: MovieDetailEntity)

    //Favorite
    // Favorite
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE imdbId = :movieId)")
    fun observeIsFavorite(movieId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteMovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItems(favorites: List<FavoriteMovieEntity>)

    @Query("DELETE FROM favorite_movies WHERE imdbId = :movieId")
    suspend fun deleteFavorite(movieId: String)

    @Query("DELETE FROM favorite_movies")
    suspend fun clearFavorites()

    @Transaction
    suspend fun replaceFavoriteMovies(
        movies: List<MovieEntity>,
        favorites: List<FavoriteMovieEntity>
    ) {
        upsertMovies(movies)
        clearFavorites()

        if (favorites.isNotEmpty()) {
            insertFavoriteItems(favorites)
        }
    }

    @Query(
        """
    SELECT movies.* FROM movies
    INNER JOIN favorite_movies
    ON movies.imdbId = favorite_movies.imdbId
    ORDER BY movies.title COLLATE NOCASE ASC
    """
    )
    fun observeFavoriteMovies(): Flow<List<MovieEntity>>

    //Brojanje favorita
    @Query("SELECT COUNT(*) FROM favorite_movies")
    fun observeFavoriteCount(): Flow<Int>




    //Watchlist
    // Watchlist
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_movies WHERE imdbId = :movieId)")
    fun observeIsInWatchlist(movieId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: WatchlistMovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItems(watchlist: List<WatchlistMovieEntity>)

    @Query("DELETE FROM watchlist_movies WHERE imdbId = :movieId")
    suspend fun deleteWatchlist(movieId: String)

    @Query("DELETE FROM watchlist_movies")
    suspend fun clearWatchlist()

    @Transaction
    suspend fun replaceWatchlistMovies(
        movies: List<MovieEntity>,
        watchlist: List<WatchlistMovieEntity>
    ) {
        upsertMovies(movies)
        clearWatchlist()

        if (watchlist.isNotEmpty()) {
            insertWatchlistItems(watchlist)
        }
    }

    @Query(
        """
    SELECT movies.* FROM movies
    INNER JOIN watchlist_movies
    ON movies.imdbId = watchlist_movies.imdbId
    ORDER BY movies.title COLLATE NOCASE ASC
    """
    )
    fun observeWatchlistMovies(): Flow<List<MovieEntity>>

    @Query("SELECT COUNT(*) FROM watchlist_movies")
    fun observeWatchlistCount(): Flow<Int>

    //Upit za kviz
    @Query(
        """
    SELECT * FROM movies
    WHERE 
        (posterUrl IS NOT NULL AND posterUrl != '')
        OR
        (backdropUrl IS NOT NULL AND backdropUrl != '')
    ORDER BY RANDOM()
    LIMIT 100
    """
    )
    suspend fun getQuizMoviePool(): List<MovieEntity>


    //Upiti za glumce
    @Query("SELECT * FROM movie_cast WHERE movieId = :movieId")
    suspend fun getCastForMovie(movieId: String): List<MovieCastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCast(cast: List<MovieCastEntity>)

    @Query("DELETE FROM movie_cast WHERE movieId = :movieId")
    suspend fun clearCastForMovie(movieId: String)

    //Upit za pogresne glumce
    @Query(
        """
    SELECT * FROM movie_cast
    WHERE movieId != :movieId
    AND name != :actorName
    AND department = 'Acting'
    GROUP BY name
    ORDER BY RANDOM()
    LIMIT 20
    """
    )
    suspend fun getRandomActorsExcept(
        movieId: String,
        actorName: String
    ): List<MovieCastEntity>

    //glumci za odredjeni film
    @Query("SELECT * FROM movie_cast WHERE movieId = :movieId AND department = 'Acting'")
    fun observeCastForMovie(movieId: String): Flow<List<MovieCastEntity>>

    @Query("SELECT * FROM movie_details WHERE imdbId = :movieId LIMIT 1")
    suspend fun getMovieDetail(movieId: String): MovieDetailEntity?
}