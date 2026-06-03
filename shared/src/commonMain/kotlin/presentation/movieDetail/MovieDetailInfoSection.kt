package presentation.movieDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import domain.model.MovieDetail

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailInfoSection(
    movie: MovieDetail,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (isDesktop) 8.dp else 6.dp
        )
    ) {
        Text(
            text = movie.title,
            style = if (isDesktop) {
                MaterialTheme.typography.headlineSmall
            } else {
                MaterialTheme.typography.titleMedium
            },
            fontWeight = FontWeight.SemiBold,
            maxLines = if (isDesktop) 3 else 2,
            overflow = TextOverflow.Ellipsis
        )

        val basicInfo = buildList {
            movie.year?.let { add("$it") }
            movie.runtime?.let { add("$it min") }
            movie.imdbRating?.let { add("IMDb $it") }
        }

        if (basicInfo.isNotEmpty()) {
            Text(
                text = basicInfo.joinToString(" · "),
                style = if (isDesktop) {
                    MaterialTheme.typography.bodyLarge
                } else {
                    MaterialTheme.typography.bodyMedium
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (movie.genres.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                movie.genres
                    .take(if (isDesktop) 8 else 4)
                    .forEach { genre ->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = genre,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        )
                    }
            }
        }
    }
}

@Composable
fun MoviePoster(
    posterUrl: String?,
    title: String,
    isDesktop: Boolean
) {
    val width = if (isDesktop) 180.dp else 96.dp
    val height = if (isDesktop) 260.dp else 142.dp

    ElevatedCard {
        if (posterUrl != null) {
            AsyncImage(
                model = posterUrl,
                contentDescription = title,
                modifier = Modifier
                    .width(width)
                    .height(height),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "No poster",
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .padding(10.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MovieOverviewSection(
    tagline: String?,
    overview: String?,
    isDesktop: Boolean
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                if (isDesktop) 16.dp else 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(
                if (isDesktop) 10.dp else 8.dp
            )
        ) {
            Text(
                text = "Overview",
                style = if (isDesktop) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.titleMedium
                }
            )

            if (!tagline.isNullOrBlank()) {
                Text(
                    text = tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = if (overview.isNullOrBlank()) {
                    "Overview is not available."
                } else {
                    overview
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}