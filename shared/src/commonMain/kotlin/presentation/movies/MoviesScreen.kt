package presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.movies.mvi.MoviesIntent
import presentation.movies.mvi.MoviesState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import presentation.common.formFieldNavigation

@Composable
fun MoviesScreen(
    state: MoviesState,
    onIntent: (MoviesIntent) -> Unit,
    onLogoutClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    var showFilters by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val hasActiveFilters =
        state.query.isNotBlank() ||
                state.minRating.isNotBlank() ||
                state.minYear.isNotBlank() ||
                state.maxYear.isNotBlank() ||
                state.selectedGenreId != null ||
                state.sortBy != "popularity" ||
                state.sortOrder != "desc"

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp
        val contentMaxWidth = if (isDesktop) 1180.dp else maxWidth

        val gridColumns = if (isDesktop) {
            GridCells.Adaptive(minSize = 260.dp)
        } else {
            GridCells.Fixed(1)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(top = if (isDesktop) 20.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
            ) {
                MoviesHeader(
                    isDesktop = isDesktop,
                    onFavoritesClick = onFavoritesClick,
                    onWatchlistClick = onWatchlistClick,
                    onProfileClick = onProfileClick,
                    onQuizClick = onQuizClick,
                    onLogoutClick = onLogoutClick
                )

                Spacer(Modifier.height(if (isDesktop) 16.dp else 12.dp))

                MoviesSearchSection(
                    state = state,
                    isDesktop = isDesktop,
                    showFilters = showFilters,
                    onToggleFilters = {
                        showFilters = !showFilters
                    },
                    onIntent = onIntent
                )

                if (showFilters) {
                    Spacer(Modifier.height(12.dp))

                    MoviesFilterPanel(
                        state = state,
                        isDesktop = isDesktop,
                        onIntent = onIntent,
                        onClose = {
                            showFilters = false
                        }
                    )
                }

                if (state.isOffline) {
                    Spacer(Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Offline mode: showing cached movies.",
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
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.errorMessage != null && state.movies.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    state.movies.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (hasActiveFilters) {
                                    "No movies match your search or filters."
                                } else {
                                    "No movies found."
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = gridColumns,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = if (isDesktop) 24.dp else 96.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(
                                items = state.movies,
                                key = { movie -> movie.imdbId }
                            ) { movie ->
                                MovieCard(
                                    title = movie.title,
                                    year = movie.year,
                                    rating = movie.imdbRating,
                                    genres = movie.genres,
                                    posterUrl = movie.posterUrl,
                                    isDesktop = isDesktop,
                                    onClick = {
                                        onMovieClick(movie.imdbId)
                                    }
                                )
                            }

                            item(
                                span = {
                                    GridItemSpan(maxLineSpan)
                                }
                            ) {
                                val canLoadMore =
                                    state.totalPages == null || state.page < state.totalPages

                                if (canLoadMore) {
                                    if (state.isLoadingMore) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                onIntent(MoviesIntent.LoadNextPage)
                                            },
                                            enabled = !state.isLoading && !state.isLoadingMore,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Load more")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}