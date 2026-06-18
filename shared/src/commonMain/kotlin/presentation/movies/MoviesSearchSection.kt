package presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import presentation.common.formFieldNavigation

@Composable
fun MoviesSearchSection(
    state: MoviesState,
    isDesktop: Boolean,
    showFilters: Boolean,
    onToggleFilters: () -> Unit,
    onIntent: (MoviesIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    if (isDesktop) {
        DesktopMoviesSearchSection(
            state = state,
            showFilters = showFilters,
            onToggleFilters = onToggleFilters,
            onIntent = onIntent
        )
    } else {
        MobileMoviesSearchSection(
            state = state,
            showFilters = showFilters,
            onToggleFilters = onToggleFilters,
            onIntent = onIntent
        )
    }
}

@Composable
private fun DesktopMoviesSearchSection(
    state: MoviesState,
    showFilters: Boolean,
    onToggleFilters: () -> Unit,
    onIntent: (MoviesIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                onIntent(MoviesIntent.SearchChanged(it))
            },
            label = {
                Text("Search movies")
            },
            singleLine = true,
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onIntent(MoviesIntent.SearchSubmitted)
                }
            ),
            modifier = Modifier
                .weight(1f)
                .formFieldNavigation(
                    focusManager = focusManager,
                    onSubmit = {
                        focusManager.clearFocus()
                        onIntent(MoviesIntent.SearchSubmitted)
                    }
                )
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                onIntent(MoviesIntent.SearchSubmitted)
            },
            enabled = !state.isLoading
        ) {
            Text("Search")
        }

        OutlinedButton(
            onClick = onToggleFilters
        ) {
            Text(
                if (showFilters) {
                    "Hide filters"
                } else {
                    "Filters"
                }
            )
        }

        OutlinedButton(
            onClick = {
                focusManager.clearFocus()
                onIntent(MoviesIntent.ClearAll)
            },
            enabled = !state.isLoading
        ) {
            Text("Clear")
        }
    }
}

@Composable
private fun MobileMoviesSearchSection(
    state: MoviesState,
    showFilters: Boolean,
    onToggleFilters: () -> Unit,
    onIntent: (MoviesIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                onIntent(MoviesIntent.SearchChanged(it))
            },
            label = {
                Text("Search movies")
            },
            singleLine = true,
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onIntent(MoviesIntent.SearchSubmitted)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .formFieldNavigation(
                    focusManager = focusManager,
                    onSubmit = {
                        focusManager.clearFocus()
                        onIntent(MoviesIntent.SearchSubmitted)
                    }
                )
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    onIntent(MoviesIntent.SearchSubmitted)
                },
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }

            OutlinedButton(
                onClick = onToggleFilters,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    if (showFilters) {
                        "Hide"
                    } else {
                        "Filters"
                    }
                )
            }

            OutlinedButton(
                onClick = {
                    onIntent(MoviesIntent.ClearAll)
                },
                enabled = !state.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }
    }
}