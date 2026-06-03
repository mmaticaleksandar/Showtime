package presentation.movieDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieDetailActions(
    isFavorite: Boolean,
    isInWatchlist: Boolean,
    isDesktop: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            if (isDesktop) 10.dp else 8.dp
        )
    ) {
        if (isFavorite) {
            OutlinedButton(
                onClick = onFavoriteClick,
                modifier = if (isDesktop) {
                    Modifier
                } else {
                    Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                }
            ) {
                Text(
                    if (isDesktop) {
                        "Remove Favorite"
                    } else {
                        "Unfavorite"
                    }
                )
            }
        } else {
            Button(
                onClick = onFavoriteClick,
                modifier = if (isDesktop) {
                    Modifier
                } else {
                    Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                }
            ) {
                Text(
                    if (isDesktop) {
                        "Add Favorite"
                    } else {
                        "Favorite"
                    }
                )
            }
        }

        if (isInWatchlist) {
            OutlinedButton(
                onClick = onWatchlistClick,
                modifier = if (isDesktop) {
                    Modifier
                } else {
                    Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                }
            ) {
                Text(
                    if (isDesktop) {
                        "Remove Watchlist"
                    } else {
                        "Remove"
                    }
                )
            }
        } else {
            Button(
                onClick = onWatchlistClick,
                modifier = if (isDesktop) {
                    Modifier
                } else {
                    Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                }
            ) {
                Text(
                    if (isDesktop) {
                        "Add Watchlist"
                    } else {
                        "Watchlist"
                    }
                )
            }
        }
    }
}