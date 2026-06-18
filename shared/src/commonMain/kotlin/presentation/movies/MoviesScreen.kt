package presentation.movies

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import presentation.movies.mvi.MoviesIntent
import presentation.movies.mvi.MoviesState

@Composable
fun MoviesScreen(
    state: MoviesState,
    onIntent: (MoviesIntent) -> Unit,
    onLogoutClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onFavoritesClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    initialScrollIndex: Int,
    initialScrollOffset: Int,
    onSaveScrollPosition: (Int, Int) -> Unit
) {
    var showFilters by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val drawerScope = rememberCoroutineScope()

    fun closeDrawerAndNavigate(action: () -> Unit) {
        drawerScope.launch {
            drawerState.close()
        }

        action()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MoviesDrawerContent(
                onFavoritesClick = {
                    closeDrawerAndNavigate {
                        onFavoritesClick()
                    }
                },
                onWatchlistClick = {
                    closeDrawerAndNavigate {
                        onWatchlistClick()
                    }
                },
                onProfileClick = {
                    closeDrawerAndNavigate {
                        onProfileClick()
                    }
                },
                onQuizClick = {
                    closeDrawerAndNavigate {
                        onQuizClick()
                    }
                },
                onLogoutClick = {
                    closeDrawerAndNavigate {
                        onLogoutClick()
                    }
                }
            )
        }
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val isDesktop = maxWidth >= 900.dp

            val contentMaxWidth = if (isDesktop) {
                1180.dp
            } else {
                maxWidth
            }

            MoviesMainContent(
                state = state,
                isDesktop = isDesktop,
                contentMaxWidth = contentMaxWidth,
                showFilters = showFilters,
                onToggleFilters = {
                    showFilters = !showFilters
                },
                onCloseFilters = {
                    showFilters = false
                },
                onMenuClick = {
                    drawerScope.launch {
                        drawerState.open()
                    }
                },
                onMovieClick = onMovieClick,
                onIntent = onIntent,
                initialScrollIndex = initialScrollIndex,
                initialScrollOffset = initialScrollOffset,
                onSaveScrollPosition = onSaveScrollPosition
            )
        }
    }
}