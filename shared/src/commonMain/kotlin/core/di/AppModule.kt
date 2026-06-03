package core.di

import data.local.auth.InMemoryTokenStorage
import data.local.auth.TokenStorage
import data.remote.auth.AuthRemoteDataSource
import data.remote.network.HttpClientFactory
import data.repository.AuthRepository
import presentation.auth.mvi.AuthStore
import data.remote.movies.MoviesRemoteDataSource
import data.repository.MoviesRepository
import presentation.movies.mvi.MoviesStore
import data.local.DatabaseFactory
import data.local.ShowtimeDatabase
import data.repository.QuizQuestionGenerator
import presentation.movieDetail.mvi.MovieDetailStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import presentation.favorites.mvi.FavoritesStore
import presentation.watchlist.mvi.WatchlistStore
import presentation.profile.mvi.ProfileStore
import data.repository.QuizRepository
import presentation.quiz.mvi.QuizStore

object AppModule {

    private var tokenStorage: TokenStorage = InMemoryTokenStorage()

    private var databaseFactory: DatabaseFactory? = null

    fun setDatabaseFactory(factory: DatabaseFactory) {
        databaseFactory = factory
    }

    private val database: ShowtimeDatabase by lazy {
        databaseFactory
            ?.createDatabase()
            ?: error("DatabaseFactory is not initialized")
    }

    private val _unauthorizedEvents = MutableSharedFlow<Unit>()

    val unauthorizedEvents: SharedFlow<Unit> =
        _unauthorizedEvents.asSharedFlow()

    fun setTokenStorage(storage: TokenStorage) {
        tokenStorage = storage
    }

    fun init(
        tokenStorage: TokenStorage
    ) {
        this.tokenStorage = tokenStorage
    }

    private fun requireTokenStorage(): TokenStorage {
        return tokenStorage

    }

    private val client by lazy {
        HttpClientFactory.create(
            tokenStorage = requireTokenStorage(),
            onUnauthorized = {
                requireTokenStorage().clearToken()
                database.movieDao().clearFavorites()
                database.movieDao().clearWatchlist()
                _unauthorizedEvents.emit(Unit)
            }
        )
    }
    private val authRemoteDataSource by lazy {
        AuthRemoteDataSource(client)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(
            remoteDataSource = authRemoteDataSource,
            tokenStorage = tokenStorage,
            movieDao = database.movieDao()
        )
    }

    fun provideAuthStore(): AuthStore {
        return AuthStore(
            authRepository = authRepository,
            moviesRepository = moviesRepository
        )
    }

    private val moviesRemoteDataSource by lazy {
        MoviesRemoteDataSource(client)
    }

    private val moviesRepository by lazy {
        MoviesRepository(
            remoteDataSource = moviesRemoteDataSource,
            movieDao = database.movieDao()
        )
    }

    fun provideMoviesStore(): MoviesStore {
        return MoviesStore(moviesRepository)
    }

    fun provideMovieDetailStore(): MovieDetailStore {
        return MovieDetailStore(moviesRepository)
    }

    fun provideFavoritesStore(): FavoritesStore {
        return FavoritesStore(moviesRepository)
    }

    fun provideWatchlistStore(): WatchlistStore {
        return WatchlistStore(moviesRepository)
    }
    fun provideProfileStore(): ProfileStore {
        return ProfileStore(
            authRepository = authRepository,
            moviesRepository = moviesRepository,
            quizRepository = quizRepository
        )
    }
    private val quizRepository by lazy {
        QuizRepository(
            database.quizDao()
        )
    }

    fun provideQuizRepository(): QuizRepository {
        return quizRepository
    }

    private val quizQuestionGenerator by lazy {
        QuizQuestionGenerator(
            database.movieDao(),
            moviesRepository = moviesRepository
        )
    }

    fun provideQuizQuestionGenerator(): QuizQuestionGenerator {
        return quizQuestionGenerator
    }
    fun provideQuizStore(): QuizStore {
        return QuizStore(
            quizQuestionGenerator = quizQuestionGenerator,
            quizRepository = quizRepository,
            authRepository = authRepository
        )
    }

}