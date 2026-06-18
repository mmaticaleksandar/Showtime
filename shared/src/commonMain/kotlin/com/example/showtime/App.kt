package com.example.showtime

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import presentation.auth.AuthLandingScreen
import presentation.auth.LoginScreen
import presentation.auth.RegisterScreen
import presentation.auth.mvi.AuthEffect
import presentation.movies.MoviesScreen
import presentation.auth.mvi.AuthIntent
import presentation.movies.mvi.MoviesIntent
import core.di.AppModule
import presentation.movieDetail.MovieDetailScreen
import presentation.movieDetail.mvi.MovieDetailIntent
import presentation.favorites.FavoritesScreen
import presentation.watchlist.WatchlistScreen
import presentation.favorites.mvi.FavoritesIntent
import presentation.movieDetail.mvi.MovieDetailEffect
import presentation.watchlist.mvi.WatchlistIntent
import presentation.profile.ProfileScreen
import presentation.profile.mvi.ProfileIntent
import presentation.quiz.QuizScreen
import presentation.quiz.QuizResultScreen
import presentation.quiz.mvi.QuizEffect
import presentation.quiz.mvi.QuizIntent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import presentation.favorites.mvi.FavoritesEffect
import presentation.profile.mvi.ProfileEffect
import presentation.watchlist.mvi.WatchlistEffect
import presentation.auth.AuthCheckingScreen
import presentation.quiz.QuizSetupScreen
import domain.model.QuizCategory
import presentation.theme.ShowtimeTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import presentation.common.AppCloser
import presentation.common.SystemBackHandler


@Composable
fun App(appCloser: AppCloser?=null) {
    ShowtimeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){

            val coroutineScope = rememberCoroutineScope()

            var lastBackPressTime by remember {
                mutableStateOf(0L)
            }
            val authStore = remember {
                AppModule.provideAuthStore()
            }

            //Filmovi store
            val moviesStore = remember {
                AppModule.provideMoviesStore()
            }

            //Stanje filmova
            val moviesState by moviesStore.state.collectAsState()

            //Stanje autorizacije
            val authState by authStore.state.collectAsState()

            val scope = rememberCoroutineScope()

            //Trenutni ekran
            var currentScreen by remember { mutableStateOf("checking_auth") }

            //Izavran film
            var selectedMovieId by remember { mutableStateOf<String?>(null) }
            var moviesScrollIndex by remember {
                mutableStateOf(0)
            }

            var moviesScrollOffset by remember {
                mutableStateOf(0)
            }

            //Detalji store
            val movieDetailStore = remember {
                AppModule.provideMovieDetailStore()
            }

            val movieDetailState by
            movieDetailStore.state.collectAsState()

            //Favorite store
            val favoritesStore = remember {
                AppModule.provideFavoritesStore()
            }

            val favoritesState by favoritesStore.state.collectAsState()

            //Watchlist store
            val watchlistStore = remember {
                AppModule.provideWatchlistStore()
            }

            val watchlistState by
            watchlistStore.state.collectAsState()

            //Profile store
            val profileStore = remember {
                AppModule.provideProfileStore()
            }

            val profileState by profileStore.state.collectAsState()

            //Kviz store
            val quizStore = remember {
                AppModule.provideQuizStore()
            }

            val quizState by quizStore.state.collectAsState()

            //Prethodni ekran
            var previousScreen by remember {
                mutableStateOf("movies")
            }
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect("auth_effects") {
                authStore.effect.collect { effect ->
                    when (effect) {
                        AuthEffect.NavigateToMovies -> {
                            currentScreen = "movies"
                        }

                        is AuthEffect.ShowMessage -> {
                            snackbarHostState.showSnackbar(effect.message)
                        }

                        AuthEffect.NavigateToAuth->{currentScreen = "auth"}
                    }
                }
            }
            LaunchedEffect("check_auth") {
                authStore.onIntent(AuthIntent.CheckAuthStatus)
            }
                var hasLoadedMoviesOnce by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(currentScreen) {
                    if (currentScreen == "movies" && !hasLoadedMoviesOnce) {
                        hasLoadedMoviesOnce = true
                        moviesStore.onIntent(MoviesIntent.LoadMovies)
                    }
                }

            LaunchedEffect("unauthorized_events") {
                AppModule.unauthorizedEvents.collect {
                    authStore.onIntent(AuthIntent.ForceLogout)
                    currentScreen = "auth"
                }
            }
            LaunchedEffect(Unit) {
                movieDetailStore.effect.collect { effect ->
                    when (effect) {
                        is MovieDetailEffect.ShowMessage -> {
                            snackbarHostState.showSnackbar(effect.message)
                        }
                    }
                }
            }

            LaunchedEffect("favorites_effects") {
                favoritesStore.effect.collect { effect ->
                    when (effect) {
                        is FavoritesEffect.ShowMessage -> {
                            snackbarHostState.showSnackbar(effect.message)
                        }
                    }
                }
            }

            LaunchedEffect("watchlist_effects") {
                watchlistStore.effect.collect { effect ->
                    when (effect) {
                        is WatchlistEffect.ShowMessage -> {
                            snackbarHostState.showSnackbar(effect.message)
                        }
                    }
                }
            }

            fun handleSystemBack() {
                when (currentScreen) {
                    "quiz" -> {
                        if (!quizState.isFinished && !quizState.isLoading && quizState.errorMessage == null) {
                            quizStore.onIntent(QuizIntent.BackClicked)
                        } else {
                            currentScreen = "movies"
                        }
                    }

                    "movie_detail" -> {
                        currentScreen = previousScreen
                    }

                    "favorites" -> {
                        currentScreen = "movies"
                    }

                    "watchlist" -> {
                        currentScreen = "movies"
                    }

                    "profile" -> {
                        currentScreen = "movies"
                    }

                    "quiz_result" -> {
                        currentScreen = "movies"
                    }
                    "quiz_setup"->{
                        currentScreen = "movies"
                    }
                    "login" -> {
                        currentScreen = "auth"
                    }

                    "register" -> {
                        currentScreen = "auth"
                    }

                    "movies", "auth" -> {
                        val now = System.currentTimeMillis()

                        if (now - lastBackPressTime < 2000L) {
                            appCloser?.closeApp()
                        } else {
                            lastBackPressTime = now

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Press back again to exit"
                                )
                            }
                        }
                    }
                }
            }

            Scaffold(
                containerColor =  MaterialTheme.colorScheme.background,
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                }
            ) { paddingValues ->

                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.padding(paddingValues)
                )
                {   SystemBackHandler(
                    enabled = true,
                    onBack = {
                        handleSystemBack()
                    }
                )
                    when (currentScreen) {
                        "checking_auth" -> AuthCheckingScreen()
                        "auth" -> AuthLandingScreen(
                            onLoginClick = { currentScreen = "login" },
                            onRegisterClick = { currentScreen = "register" }
                        )

                        "login" -> LoginScreen(
                            state = authState,
                            onIntent = { intent ->
                                scope.launch {
                                    try {
                                        authStore.onIntent(intent)
                                    } catch (e: Exception) {
                                        print(e.message)
                                    }
                                }
                            },
                            onBackClick = {
                                currentScreen = "auth"
                            },
                            onRegisterClick = {
                                currentScreen = "register"
                            }
                        )

                        "register" -> RegisterScreen(
                            state = authState,
                            onIntent = { intent ->
                                scope.launch {
                                    try {
                                        authStore.onIntent(intent)
                                    } catch (e: Exception) {
                                        print(e.message)
                                    }
                                }
                            },
                            onBackClick = { currentScreen = "auth" },
                            onLoginClick = { currentScreen = "login" }
                        )

                        "movies" -> MoviesScreen(
                            state = moviesState,
                            onIntent = { intent ->
                                moviesStore.onIntent(intent)
                            },
                            onLogoutClick = {
                                scope.launch {
                                    authStore.onIntent(AuthIntent.LogoutClicked)
                                }
                            },
                            onMovieClick = { movieId ->
                                previousScreen = "movies"

                                selectedMovieId = movieId
                                currentScreen = "movie_detail"
                            },
                            onFavoritesClick = {
                                moviesStore.onIntent(MoviesIntent.StopObservingMovies)
                                favoritesStore.onIntent(FavoritesIntent.LoadFavorites)
                                currentScreen = "favorites"
                            },
                            onWatchlistClick = {
                                moviesStore.onIntent(MoviesIntent.StopObservingMovies)
                                watchlistStore.onIntent(WatchlistIntent.LoadWatchlist)
                                currentScreen = "watchlist"
                            },
                            onProfileClick = {
                                currentScreen = "profile"
                            },
                            onQuizClick = {
                                currentScreen = "quiz_setup"
                            },
                            initialScrollIndex = moviesScrollIndex,
                            initialScrollOffset = moviesScrollOffset,
                            onSaveScrollPosition = { index, offset ->
                                moviesScrollIndex = index
                                moviesScrollOffset = offset
                            }
                        )

                        "movie_detail" -> {

                            LaunchedEffect(
                                selectedMovieId
                            ) {

                                selectedMovieId?.let {

                                    movieDetailStore.onIntent(
                                        MovieDetailIntent
                                            .LoadMovieDetail(it)
                                    )
                                }
                            }

                            MovieDetailScreen(
                                state = movieDetailState,

                                //Back dugme klik
                                onBackClick = {
                                    currentScreen = previousScreen
                                },

                                //Favorite dugme klik
                                onFavoriteClick = {
                                    movieDetailStore.onIntent(
                                        MovieDetailIntent.ToggleFavorite
                                    )
                                },

                                //Watchlist dugme klik
                                onWatchlistClick = {
                                    movieDetailStore.onIntent(
                                        MovieDetailIntent.ToggleWatchlist
                                    )
                                }

                            )


                        }

                        "favorites" -> {
                            FavoritesScreen(
                                state = favoritesState,
                                onBackClick = {
                                    currentScreen = "movies"
                                },
                                onMovieClick = { movieId ->
                                    previousScreen = "favorites"

                                    selectedMovieId = movieId
                                    currentScreen = "movie_detail"
                                },
                                onRemoveClick = { movieId ->
                                    favoritesStore.onIntent(
                                        FavoritesIntent.RemoveFavorite(movieId)
                                    )
                                }
                            )
                        }

                        "watchlist" -> {
                            WatchlistScreen(
                                state = watchlistState,
                                onBackClick = {
                                    currentScreen = "movies"
                                },
                                onMovieClick = { movieId ->
                                    previousScreen = "watchlist"
                                    selectedMovieId = movieId
                                    currentScreen = "movie_detail"
                                },
                                onRemoveClick = { movieId ->
                                    watchlistStore.onIntent(
                                        WatchlistIntent.RemoveWatchlist(movieId)
                                    )
                                }
                            )

                        }

                        "profile" -> {
                            LaunchedEffect("profile") {
                                profileStore.onIntent(ProfileIntent.LoadProfile)
                            }
                            LaunchedEffect(profileStore) {
                                profileStore.effect.collect { effect ->
                                    when (effect) {
                                        ProfileEffect.NavigateToAuth -> {
                                            currentScreen = "auth"
                                        }
                                    }
                                }
                            }
                            ProfileScreen(
                                state = profileState,
                                onBackClick = {
                                    currentScreen = "movies"
                                },
                                onLogoutClick = {
                                    profileStore.onIntent(ProfileIntent.LogoutClicked)
                                }
                            )
                        }
                        "quiz_setup" -> {
                            QuizSetupScreen(
                                onBackClick = {
                                    currentScreen = "movies"
                                },
                                onCategorySelected = { category ->
                                    currentScreen = "quiz"

                                    quizStore.onIntent(
                                        QuizIntent.StartQuiz(
                                            category = category
                                        )
                                    )
                                }
                            )
                        }

                        "quiz" -> {
                            QuizScreen(
                                state = quizState,
                                onIntent = { intent ->
                                    quizStore.onIntent(intent)
                                },
                                onBackClick = { currentScreen = "movies" }

                            )
                            LaunchedEffect(Unit) {
                                quizStore.effect.collect { effect ->
                                    when (effect) {
                                        QuizEffect.NavigateToResult -> {
                                            currentScreen = "quiz_result"
                                        }

                                        QuizEffect.NavigateToMovies -> {
                                            currentScreen = "movies"
                                        }
                                    }
                                }
                            }
                        }

                        "quiz_result" -> {
                            QuizResultScreen(
                                state = quizState,
                                onBackToMoviesClick = {
                                    currentScreen = "movies"
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}