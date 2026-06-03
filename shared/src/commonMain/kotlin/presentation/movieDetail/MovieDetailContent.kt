package presentation.movieDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.MovieDetail

@Composable
fun MovieDetailContent(
    movie: MovieDetail,
    cast: List<String>,
    isFavorite: Boolean,
    isInWatchlist: Boolean,
    isDesktop: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit
) {
    val bottomPadding = if (isDesktop) 24.dp else 96.dp
    val sectionSpacing = if (isDesktop) 16.dp else 12.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = bottomPadding)
    ) {
        MovieDetailHero(
            backdropUrl = movie.backdropUrl,
            title = movie.title,
            isDesktop = isDesktop
        )

        Spacer(Modifier.height(sectionSpacing))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isDesktop) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    MoviePoster(
                        posterUrl = movie.posterUrl,
                        title = movie.title,
                        isDesktop = true
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MovieDetailInfoSection(
                            movie = movie,
                            isDesktop = true
                        )

                        MovieDetailActions(
                            isFavorite = isFavorite,
                            isInWatchlist = isInWatchlist,
                            isDesktop = true,
                            onFavoriteClick = onFavoriteClick,
                            onWatchlistClick = onWatchlistClick
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MoviePoster(
                            posterUrl = movie.posterUrl,
                            title = movie.title,
                            isDesktop = false
                        )

                        MovieDetailInfoSection(
                            movie = movie,
                            isDesktop = false,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    MovieDetailActions(
                        isFavorite = isFavorite,
                        isInWatchlist = isInWatchlist,
                        isDesktop = false,
                        onFavoriteClick = onFavoriteClick,
                        onWatchlistClick = onWatchlistClick
                    )
                }
            }
        }

        Spacer(Modifier.height(sectionSpacing))

        MovieOverviewSection(
            tagline = movie.tagline,
            overview = movie.overview,
            isDesktop = isDesktop
        )

        Spacer(Modifier.height(sectionSpacing))

        MovieCastSection(
            cast = cast,
            isDesktop = isDesktop
        )
    }
}