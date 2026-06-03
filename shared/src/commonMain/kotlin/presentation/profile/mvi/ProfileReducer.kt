package presentation.profile.mvi

object ProfileReducer {

    fun reduce(
        state: ProfileState,
        change: ProfileChange
    ): ProfileState {
        return when (change) {
            ProfileChange.LoadingStarted -> state.copy(
                isLoading = true,
                isOffline = false,
                errorMessage = null
            )

            is ProfileChange.ProfileLoaded -> state.copy(
                fullName = change.fullName,
                username = change.username,
                isLoading = false,
                isOffline = false,
                errorMessage = null
            )

            is ProfileChange.ProfileLoadFailed -> state.copy(
                isLoading = false,
                isOffline = true,
                errorMessage = change.message
            )

            is ProfileChange.StatsChanged -> state.copy(
                favoriteCount = change.favoriteCount,
                watchlistCount = change.watchlistCount,
                bestScore = change.bestScore,
                playedQuizzes = change.playedQuizzes
            )

            ProfileChange.LoggedOut -> ProfileState()
        }
    }
}