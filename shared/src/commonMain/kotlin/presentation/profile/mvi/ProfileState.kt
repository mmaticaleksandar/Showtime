package presentation.profile.mvi

data class ProfileState(
    val fullName: String = "",
    val username: String = "",
    val bestScore: Double = 0.0,
    val playedQuizzes: Int = 0,
    val favoriteCount: Int = 0,
    val watchlistCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOffline: Boolean = false
)