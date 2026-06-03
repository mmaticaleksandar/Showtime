package presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.movies.mvi.MoviesIntent
import presentation.movies.mvi.MoviesState

private val movieGenres = listOf(
    null to "Any",
    28 to "Action",
    12 to "Adventure",
    16 to "Animation",
    35 to "Comedy",
    80 to "Crime",
    18 to "Drama",
    10751 to "Family",
    14 to "Fantasy",
    36 to "History",
    27 to "Horror",
    10402 to "Music",
    9648 to "Mystery",
    10749 to "Romance",
    878 to "Science Fiction",
    53 to "Thriller",
    10752 to "War",
    37 to "Western"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoviesFilterPanel(
    state: MoviesState,
    isDesktop: Boolean,
    onIntent: (MoviesIntent) -> Unit,
    onClose: () -> Unit
) {
    val panelMaxHeight = if (isDesktop) {
        900.dp
    } else {
        470.dp
    }

    val panelPadding = if (isDesktop) {
        14.dp
    } else {
        12.dp
    }

    val itemSpacing = if (isDesktop) {
        12.dp
    } else {
        10.dp
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = panelMaxHeight)
                .verticalScroll(rememberScrollState())
                .padding(panelPadding),
            verticalArrangement = Arrangement.spacedBy(itemSpacing)
        ) {
            Text(
                text = "Filters",
                style = if (isDesktop) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.headlineSmall
                }
            )

            Text(
                text = "Sorting",
                style = MaterialTheme.typography.titleMedium
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SelectableFilterChip(
                    text = "Popular",
                    selected = state.sortBy == "popularity",
                    onClick = {
                        onIntent(MoviesIntent.SortByChanged("popularity"))
                    }
                )

                SelectableFilterChip(
                    text = "Rating",
                    selected = state.sortBy == "rating",
                    onClick = {
                        onIntent(MoviesIntent.SortByChanged("rating"))
                    }
                )

                SelectableFilterChip(
                    text = "Year",
                    selected = state.sortBy == "year",
                    onClick = {
                        onIntent(MoviesIntent.SortByChanged("year"))
                    }
                )

                SelectableFilterChip(
                    text = "Descending",
                    selected = state.sortOrder == "desc",
                    onClick = {
                        onIntent(MoviesIntent.SortOrderChanged("desc"))
                    }
                )

                SelectableFilterChip(
                    text = "Ascending",
                    selected = state.sortOrder == "asc",
                    onClick = {
                        onIntent(MoviesIntent.SortOrderChanged("asc"))
                    }
                )
            }

            OutlinedTextField(
                value = state.minRating,
                onValueChange = {
                    onIntent(MoviesIntent.MinRatingChanged(it))
                },
                label = {
                    Text("Minimum rating")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.minYear,
                    onValueChange = {
                        onIntent(MoviesIntent.MinYearChanged(it))
                    },
                    label = {
                        Text("Min year")
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = state.maxYear,
                    onValueChange = {
                        onIntent(MoviesIntent.MaxYearChanged(it))
                    },
                    label = {
                        Text("Max year")
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Genre",
                style = MaterialTheme.typography.titleMedium
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movieGenres.forEach { (genreId, name) ->
                    SelectableFilterChip(
                        text = name,
                        selected = genreId == state.selectedGenreId,
                        onClick = {
                            onIntent(MoviesIntent.GenreChanged(genreId))
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        onIntent(MoviesIntent.ApplyFilters)
                        onClose()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply")
                }

                OutlinedButton(
                    onClick = {
                        onIntent(MoviesIntent.ClearFilters)
                        onClose()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
            }
        }
    }
}

@Composable
private fun SelectableFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(text)
        }
    )
}