package presentation.profile.mvi

import core.result.AppResult
import data.repository.AuthRepository
import data.repository.MoviesRepository
import data.repository.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ProfileStore(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository,
    private val quizRepository: QuizRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var observeProfileStatsJob: Job? = null

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    private fun reduce(change: ProfileChange) {
        _state.value = ProfileReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> loadProfile()
            ProfileIntent.LogoutClicked -> logout()
        }
    }

    private fun logout() {
        scope.launch {
            authRepository.logout()

            reduce(ProfileChange.LoggedOut)

            _effect.emit(ProfileEffect.NavigateToAuth)
        }
    }

    private fun loadProfile() {
        scope.launch {
            reduce(ProfileChange.LoadingStarted)

            when (val result = authRepository.getMe()) {
                is AppResult.Success -> {
                    val user = result.data

                    reduce(
                        ProfileChange.ProfileLoaded(
                            fullName = user.fullName,
                            username = user.username
                        )
                    )

                    observeProfileStats(user.id)
                }

                is AppResult.Error -> {
                    reduce(
                        ProfileChange.ProfileLoadFailed(
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    private fun observeProfileStats(userId: Int) {
        observeProfileStatsJob?.cancel()

        observeProfileStatsJob = scope.launch {
            combine(
                moviesRepository.observeFavoriteCount(),
                moviesRepository.observeWatchlistCount(),
                quizRepository.observeStats(userId)
            ) { favoriteCount, watchlistCount, quizStats ->
                Triple(favoriteCount, watchlistCount, quizStats)
            }.collect { (favoriteCount, watchlistCount, quizStats) ->
                reduce(
                    ProfileChange.StatsChanged(
                        favoriteCount = favoriteCount,
                        watchlistCount = watchlistCount,
                        bestScore = quizStats.bestScore,
                        playedQuizzes = quizStats.playedCount
                    )
                )
            }
        }
    }

    fun clear() {
        observeProfileStatsJob?.cancel()
        observeProfileStatsJob = null

        scope.cancel()
    }
}