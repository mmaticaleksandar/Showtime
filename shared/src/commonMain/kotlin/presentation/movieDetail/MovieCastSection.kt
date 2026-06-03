package presentation.movieDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieCastSection(
    cast: List<String>,
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
                text = "Cast",
                style = if (isDesktop) {
                    MaterialTheme.typography.titleLarge
                } else {
                    MaterialTheme.typography.titleMedium
                }
            )

            if (cast.isEmpty()) {
                Text(
                    text = "Cast is not available.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    cast.take(if (isDesktop) 12 else 8).forEach { actor ->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = actor,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}