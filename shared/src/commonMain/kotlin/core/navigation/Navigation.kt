package core.navigation

sealed class Screen(val route: String) {
    data object AuthLanding : Screen("auth_landing")
    data object Login : Screen("login")
    data object Register : Screen("register")

    data object Movies : Screen("movies")
    data object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: Int): String = "movie_detail/$movieId"
    }

    data object Favorites : Screen("favorites")
    data object Watchlist : Screen("watchlist")
    data object Quiz : Screen("quiz")
    data object QuizResult : Screen("quiz_result")
    data object Profile : Screen("profile")
}