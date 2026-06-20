package presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import presentation.movies.mvi.MoviesIntent
import presentation.movies.mvi.MoviesState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun MoviesListContent(
    state: MoviesState,
    isDesktop: Boolean,
    contentMaxWidth: Dp,
    hasActiveFilters: Boolean,
    onMovieClick: (String) -> Unit,
    onIntent: (MoviesIntent) -> Unit,
    initialScrollIndex: Int,
    initialScrollOffset: Int,
    onSaveScrollPosition: (Int, Int) -> Unit
) {
    val gridColumns = if (isDesktop) {
        GridCells.Adaptive(minSize = 260.dp)
    } else {
        GridCells.Fixed(1)
    }

    Box(
        modifier = Modifier
            .widthIn(max = contentMaxWidth)
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                MoviesLoadingState()
            }

            state.errorMessage != null && state.movies.isEmpty() -> {
                MoviesErrorState(
                    message = state.errorMessage
                )
            }

            state.movies.isEmpty() -> {
                MoviesEmptyState(
                    hasActiveFilters = hasActiveFilters
                )
            }

            else -> {
                MoviesGrid(
                    state = state,
                    isDesktop = isDesktop,
                    gridColumns = gridColumns,
                    onMovieClick = onMovieClick,
                    onIntent = onIntent,
                    initialScrollIndex = initialScrollIndex,
                    initialScrollOffset = initialScrollOffset,
                    onSaveScrollPosition = onSaveScrollPosition
                )
            }
        }
    }
}

@Composable
private fun MoviesLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MoviesErrorState(
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun MoviesEmptyState(
    hasActiveFilters: Boolean
) {
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

@Composable
private fun MoviesGrid(
    state: MoviesState,
    isDesktop: Boolean,
    gridColumns: GridCells,
    onMovieClick: (String) -> Unit,
    onIntent: (MoviesIntent) -> Unit,
    initialScrollIndex: Int,
    initialScrollOffset: Int,
    onSaveScrollPosition: (Int, Int) -> Unit
) {
    val gridState = rememberLazyGridState(
        initialFirstVisibleItemIndex = initialScrollIndex,
        initialFirstVisibleItemScrollOffset = initialScrollOffset
    )

    val canLoadMore =
        state.totalPages == null || state.page < state.totalPages

    val shouldLoadNextPage by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItems > 0 &&
                    lastVisibleItemIndex >= totalItems - 6
        }
    }

    LaunchedEffect(
        shouldLoadNextPage,
        canLoadMore,
        state.isLoading,
        state.isLoadingMore,
        state.page
    ) {
        if (
            shouldLoadNextPage &&
            canLoadMore &&
            !state.isLoading &&
            !state.isLoadingMore
        ) {
            onIntent(MoviesIntent.LoadNextPage)
        }
    }

    LazyVerticalGrid(
        columns = gridColumns,
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = if (isDesktop) 24.dp else 96.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = state.movies,
            key = { movie ->
                movie.imdbId
            }
        ) { movie ->
            MovieCard(
                title = movie.title,
                year = movie.year,
                rating = movie.imdbRating,
                genres = movie.genres,
                posterUrl = movie.posterUrl,
                isDesktop = isDesktop,
                onClick = {
                    onSaveScrollPosition(
                        gridState.firstVisibleItemIndex,
                        gridState.firstVisibleItemScrollOffset
                    )

                    onMovieClick(movie.imdbId)
                }
            )
        }

        if (canLoadMore && state.isLoadingMore) {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                MoviesLoadingMoreItem()
            }
        }
    }
}

@Composable
private fun MoviesLoadingMoreItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}