package presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileAccountCard(
    fullName: String,
    username: String,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    ProfileCardContainer(
        title = "Account",
        isDesktop = isDesktop,
        modifier = modifier
    ) {
        if (fullName.isNotBlank()) {
            ProfileInfoRow(
                label = "Full name",
                value = fullName
            )
        }

        ProfileInfoRow(
            label = "Username",
            value = username.ifBlank { "Unavailable" }
        )
    }
}

@Composable
fun ProfileLibraryCard(
    favoriteCount: Int,
    watchlistCount: Int,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    ProfileCardContainer(
        title = "Library",
        isDesktop = isDesktop,
        modifier = modifier
    ) {
        ProfileInfoRow(
            label = "Favorite movies",
            value = favoriteCount.toString()
        )

        ProfileInfoRow(
            label = "Watchlist movies",
            value = watchlistCount.toString()
        )
    }
}

@Composable
fun ProfileQuizCard(
    bestScore: Double,
    playedQuizzes: Int,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    ProfileCardContainer(
        title = "Quiz stats",
        isDesktop = isDesktop,
        modifier = modifier
    ) {
        ProfileInfoRow(
            label = "Best quiz score",
            value = bestScore.toString()
        )

        ProfileInfoRow(
            label = "Played quizzes",
            value = playedQuizzes.toString()
        )
    }
}

@Composable
fun ProfileLogoutCard(
    isDesktop: Boolean,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileCardContainer(
        title = "Session",
        isDesktop = isDesktop,
        modifier = modifier
    ) {
        if (isDesktop) {
            Text(
                text = "Logout clears your token and local user data, then returns you to the auth screen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "Logout and return to auth screen.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

@Composable
private fun ProfileCardContainer(
    title: String,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val minHeight = if (isDesktop) 170.dp else 130.dp
    val cardPadding = if (isDesktop) 16.dp else 14.dp
    val spacing = if (isDesktop) 12.dp else 10.dp

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
    ) {
        Column(
            modifier = Modifier.padding(cardPadding),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Text(
                text = title,
                style = if (isDesktop) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.SemiBold
            )

            content()
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}