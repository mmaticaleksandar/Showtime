package presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.QuizCategory

@Composable
fun QuizSetupScreen(
    onBackClick: () -> Unit,
    onCategorySelected: (QuizCategory) -> Unit
) {
    val categories = QuizCategory.entries

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp
        val contentMaxWidth = if (isDesktop) 900.dp else maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(
                    top = if (isDesktop) 20.dp else 12.dp,
                    bottom = if (isDesktop) 24.dp else 96.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
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
                            text = "Choose quiz type",
                            style = if (isDesktop) {
                                MaterialTheme.typography.headlineLarge
                            } else {
                                MaterialTheme.typography.headlineMedium
                            }
                        )

                        Text(
                            text = "Start a 10-question movie knowledge quiz.",
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

                Spacer(Modifier.height(if (isDesktop) 20.dp else 14.dp))

                categories.forEach { category ->
                    QuizCategoryCard(
                        category = category,
                        isDesktop = isDesktop,
                        onCategorySelected = onCategorySelected
                    )

                    Spacer(Modifier.height(if (isDesktop) 12.dp else 10.dp))
                }
            }
        }
    }
}

@Composable
private fun QuizCategoryCard(
    category: QuizCategory,
    isDesktop: Boolean,
    onCategorySelected: (QuizCategory) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (category.isAvailable) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isDesktop) 18.dp else 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (isDesktop) 18.dp else 12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(if (isDesktop) 6.dp else 4.dp)
            ) {
                Text(
                    text = category.title,
                    style = if (isDesktop) {
                        MaterialTheme.typography.titleLarge
                    } else {
                        MaterialTheme.typography.titleMedium
                    }
                )

                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (isDesktop) Int.MAX_VALUE else 2
                )
            }

            if (category.isAvailable) {
                Button(
                    onClick = {
                        onCategorySelected(category)
                    },
                    modifier = Modifier.heightIn(min = 42.dp)
                ) {
                    Text("Start")
                }
            } else {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.heightIn(min = 42.dp)
                ) {
                    Text(
                        text = if (isDesktop) {
                            "Coming soon"
                        } else {
                            "Soon"
                        }
                    )
                }
            }
        }
    }
}