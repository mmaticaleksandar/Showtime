package presentation.movies

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoviesDrawerContent(
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(18.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(
                    text = "Showtime",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "Movie catalog & quiz",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        AnimatedDrawerItem(
            label = "Favorites",
            onClick = onFavoritesClick
        )

        AnimatedDrawerItem(
            label = "Watchlist",
            onClick = onWatchlistClick
        )

        AnimatedDrawerItem(
            label = "Profile",
            onClick = onProfileClick
        )

        AnimatedDrawerItem(
            label = "Quiz",
            onClick = onQuizClick
        )

        AnimatedDrawerItem(
            label = "Logout",
            onClick = onLogoutClick
        )
    }
}
@Composable
private fun AnimatedDrawerItem(
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val verticalPadding by animateDpAsState(
        targetValue = if (isPressed) 1.dp else 2.dp
    )

    val horizontalPadding by animateDpAsState(
        targetValue = if (isPressed) 10.dp else 12.dp
    )

    val containerColor by animateColorAsState(
        targetValue = if (isPressed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            Color.Transparent
        }
    )

    NavigationDrawerItem(
        label = {
            Text(label)
        },
        selected = false,
        onClick = onClick,
        interactionSource = interactionSource,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = containerColor,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(
            horizontal = horizontalPadding,
            vertical = verticalPadding
        )
    )
}