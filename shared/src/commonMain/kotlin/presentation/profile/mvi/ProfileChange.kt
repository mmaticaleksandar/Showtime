package presentation.profile.mvi

sealed interface ProfileChange {

    data object LoadingStarted : ProfileChange

    data class ProfileLoaded(
        val fullName: String,
        val username: String
    ) : ProfileChange

    data class ProfileLoadFailed(
        val message: String
    ) : ProfileChange

    data class StatsChanged(
        val favoriteCount: Int,
        val watchlistCount: Int,
        val bestScore: Double,
        val playedQuizzes: Int
    ) : ProfileChange

    data object LoggedOut : ProfileChange
}