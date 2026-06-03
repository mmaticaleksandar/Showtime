package presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.profile.mvi.ProfileState
import kotlin.math.round

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileContent(
    state: ProfileState,
    isDesktop: Boolean,
    onLogoutClick: () -> Unit
) {
    val roundedBestScore = round(state.bestScore * 100) / 100
    val bottomPadding = if (isDesktop) 24.dp else 96.dp
    val spacing = if (isDesktop) 16.dp else 12.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = bottomPadding),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        ProfileStatusBanner(
            state = state
        )

        if (isDesktop) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 2
            ) {
                ProfileAccountCard(
                    fullName = state.fullName,
                    username = state.username,
                    isDesktop = true,
                    modifier = Modifier.weight(1f)
                )

                ProfileLibraryCard(
                    favoriteCount = state.favoriteCount,
                    watchlistCount = state.watchlistCount,
                    isDesktop = true,
                    modifier = Modifier.weight(1f)
                )

                ProfileQuizCard(
                    bestScore = roundedBestScore,
                    playedQuizzes = state.playedQuizzes,
                    isDesktop = true,
                    modifier = Modifier.weight(1f)
                )

                ProfileLogoutCard(
                    isDesktop = true,
                    onLogoutClick = onLogoutClick,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            ProfileAccountCard(
                fullName = state.fullName,
                username = state.username,
                isDesktop = false,
                modifier = Modifier.fillMaxWidth()
            )

            ProfileLibraryCard(
                favoriteCount = state.favoriteCount,
                watchlistCount = state.watchlistCount,
                isDesktop = false,
                modifier = Modifier.fillMaxWidth()
            )

            ProfileQuizCard(
                bestScore = roundedBestScore,
                playedQuizzes = state.playedQuizzes,
                isDesktop = false,
                modifier = Modifier.fillMaxWidth()
            )

            ProfileLogoutCard(
                isDesktop = false,
                onLogoutClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProfileStatusBanner(
    state: ProfileState
) {
    when {
        state.isOffline -> {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "You are offline. Showing local profile statistics.",
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        state.errorMessage != null -> {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = state.errorMessage,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}