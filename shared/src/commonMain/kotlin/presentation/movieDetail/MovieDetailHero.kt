package presentation.movieDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun MovieDetailHero(
    backdropUrl: String?,
    title: String,
    isDesktop: Boolean
) {
    val heroHeight = if (isDesktop) 320.dp else 170.dp

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(heroHeight),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp
    ) {
        if (backdropUrl != null) {
            AsyncImage(
                model = backdropUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No backdrop available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}