package presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun MovieCard(
    title: String,
    year: Int?,
    rating: Double?,
    genres: List<String>,
    posterUrl: String?,
    isDesktop: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            if (posterUrl != null) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            if (isDesktop) {
                                170.dp
                            } else {
                                165.dp
                            }
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = if (isDesktop) 12.dp else 10.dp,
                    vertical = if (isDesktop) 12.dp else 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    year?.let {
                        Text(
                            text = "$it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    rating?.let {
                        Text(
                            text = "IMDb $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (genres.isNotEmpty()) {
                    Text(
                        text = genres.take(3).joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}