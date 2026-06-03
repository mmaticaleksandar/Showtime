package presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import domain.model.Movie

@Composable
fun SavedMoviesScreenContent(
    title: String,
    emptyMessage: String,
    offlineMessage: String,
    isLoading: Boolean,
    errorMessage: String?,
    isOffline: Boolean,
    movies: List<Movie>,
    onBackClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp
        val contentMaxWidth = if (isDesktop) 1180.dp else maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(top = if (isDesktop) 20.dp else 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
            ) {
                SavedMoviesHeader(
                    title = title,
                    count = movies.size,
                    isDesktop = isDesktop,
                    onBackClick = onBackClick
                )

                if (isOffline && movies.isNotEmpty()) {
                    Spacer(Modifier.height(if (isDesktop) 14.dp else 10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = offlineMessage,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(Modifier.height(if (isDesktop) 16.dp else 12.dp))
            }

            Box(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage != null && movies.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    movies.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emptyMessage,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = if (isDesktop) {
                                GridCells.Adaptive(minSize = 260.dp)
                            } else {
                                GridCells.Fixed(1)
                            },
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                bottom = if (isDesktop) 24.dp else 96.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(
                                if (isDesktop) 14.dp else 10.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(
                                if (isDesktop) 14.dp else 10.dp
                            )
                        ) {
                            items(
                                items = movies,
                                key = { movie -> movie.imdbId }
                            ) { movie ->
                                SavedMovieCard(
                                    movie = movie,
                                    isDesktop = isDesktop,
                                    onClick = {
                                        onMovieClick(movie.imdbId)
                                    },
                                    onRemoveClick = {
                                        onRemoveClick(movie.imdbId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedMoviesHeader(
    title: String,
    count: Int,
    isDesktop: Boolean,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = if (isDesktop) {
                    MaterialTheme.typography.headlineLarge
                } else {
                    MaterialTheme.typography.headlineMedium
                }
            )

            Text(
                text = "$count movies",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        OutlinedButton(
            onClick = onBackClick
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun SavedMovieCard(
    movie: Movie,
    isDesktop: Boolean,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isDesktop) {
            DesktopSavedMovieCard(
                movie = movie,
                onRemoveClick = onRemoveClick
            )
        } else {
            MobileSavedMovieCard(
                movie = movie,
                onRemoveClick = onRemoveClick
            )
        }
    }
}

@Composable
private fun DesktopSavedMovieCard(
    movie: Movie,
    onRemoveClick: () -> Unit
) {
    Column {
        MoviePosterImage(
            posterUrl = movie.posterUrl,
            title = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        )

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SavedMovieInfo(movie = movie)

            OutlinedButton(
                onClick = onRemoveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove")
            }
        }
    }
}

@Composable
private fun MobileSavedMovieCard(
    movie: Movie,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MoviePosterImage(
            posterUrl = movie.posterUrl,
            title = movie.title,
            modifier = Modifier
                .width(78.dp)
                .height(116.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SavedMovieInfo(movie = movie)

            OutlinedButton(
                onClick = onRemoveClick,
                modifier = Modifier.heightIn(min = 40.dp)
            ) {
                Text("Remove")
            }
        }
    }
}

@Composable
private fun SavedMovieInfo(
    movie: Movie
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        val info = buildList {
            movie.year?.let { add("$it") }
            movie.imdbRating?.let { add("IMDb $it") }
        }

        if (info.isNotEmpty()) {
            Text(
                text = info.joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (movie.genres.isNotEmpty()) {
            Text(
                text = movie.genres.take(3).joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MoviePosterImage(
    posterUrl: String?,
    title: String,
    modifier: Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        if (posterUrl != null) {
            AsyncImage(
                model = posterUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No image",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}