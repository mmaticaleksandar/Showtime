package presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoviesHeader(
    isDesktop: Boolean,
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    if (isDesktop) {
        DesktopMoviesHeader(
            onFavoritesClick = onFavoritesClick,
            onWatchlistClick = onWatchlistClick,
            onProfileClick = onProfileClick,
            onQuizClick = onQuizClick,
            onLogoutClick = onLogoutClick
        )
    } else {
        MobileMoviesHeader(
            onFavoritesClick = onFavoritesClick,
            onWatchlistClick = onWatchlistClick,
            onProfileClick = onProfileClick,
            onQuizClick = onQuizClick
        )
    }
}

@Composable
private fun DesktopMoviesHeader(
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Movies",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f)
        )

        OutlinedButton(onClick = onFavoritesClick) {
            Text("Favorites")
        }

        OutlinedButton(onClick = onWatchlistClick) {
            Text("Watchlist")
        }

        OutlinedButton(onClick = onProfileClick) {
            Text("Profile")
        }

        Button(onClick = onQuizClick) {
            Text("Start Quiz")
        }

        TextButton(onClick = onLogoutClick) {
            Text("Logout")
        }
    }
}

@Composable
private fun MobileMoviesHeader(
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Movies",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onFavoritesClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Favorites")
            }

            Button(
                onClick = onWatchlistClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Watchlist")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onProfileClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Profile")
            }

            Button(
                onClick = onQuizClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Quiz")
            }
        }
    }
}