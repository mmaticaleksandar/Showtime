package presentation.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import presentation.movies.mvi.MoviesIntent
import presentation.movies.mvi.MoviesState

@Composable
fun MoviesMainContent(
    state: MoviesState,
    isDesktop: Boolean,
    contentMaxWidth: Dp,
    showFilters: Boolean,
    onToggleFilters: () -> Unit,
    onCloseFilters: () -> Unit,
    onMenuClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onIntent: (MoviesIntent) -> Unit,
    initialScrollIndex: Int,
    initialScrollOffset: Int,
    onSaveScrollPosition: (Int, Int) -> Unit
) {
    val hasActiveFilters =
        state.query.isNotBlank() ||
                state.minRating.isNotBlank() ||
                state.minYear.isNotBlank() ||
                state.maxYear.isNotBlank() ||
                state.selectedGenreId != null ||
                state.sortBy != "popularity" ||
                state.sortOrder != "desc"

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
                onMenuClick = onMenuClick
            )

            Spacer(
                modifier = Modifier.height(if (isDesktop) 16.dp else 12.dp)
            )

            MoviesSearchSection(
                state = state,
                isDesktop = isDesktop,
                showFilters = showFilters,
                onToggleFilters = onToggleFilters,
                onIntent = onIntent
            )

            if (showFilters) {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                MoviesFilterPanel(
                    state = state,
                    isDesktop = isDesktop,
                    onIntent = onIntent,
                    onClose = onCloseFilters
                )
            }

            if (state.isOffline) {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

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

            Spacer(
                modifier = Modifier.height(if (isDesktop) 16.dp else 12.dp)
            )
        }

        MoviesListContent(
            state = state,
            isDesktop = isDesktop,
            contentMaxWidth = contentMaxWidth,
            hasActiveFilters = hasActiveFilters,
            onMovieClick = onMovieClick,
            onIntent = onIntent,
            initialScrollIndex = initialScrollIndex,
            initialScrollOffset = initialScrollOffset,
            onSaveScrollPosition = onSaveScrollPosition
        )
    }
}