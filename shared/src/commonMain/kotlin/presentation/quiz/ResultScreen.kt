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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import presentation.quiz.mvi.QuizState
import kotlin.math.round

@Composable
fun QuizResultScreen(
    state: QuizState,
    onBackToMoviesClick: () -> Unit
) {
    val roundedScore = round(state.score * 100) / 100

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
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(if (isDesktop) 18.dp else 12.dp)
            ) {
                Text(
                    text = "Quiz Result",
                    style = if (isDesktop) {
                        MaterialTheme.typography.headlineLarge
                    } else {
                        MaterialTheme.typography.headlineMedium
                    }
                )

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(if (isDesktop) 22.dp else 16.dp),
                        verticalArrangement = Arrangement.spacedBy(if (isDesktop) 16.dp else 10.dp)
                    ) {
                        Text(
                            text = "$roundedScore / 100",
                            style = if (isDesktop) {
                                MaterialTheme.typography.displaySmall
                            } else {
                                MaterialTheme.typography.headlineLarge
                            },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = resultMessage(roundedScore),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isDesktop) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ResultStatCard(
                            label = "Correct answers",
                            value = state.correctAnswers.toString(),
                            isDesktop = true,
                            modifier = Modifier.weight(1f)
                        )

                        ResultStatCard(
                            label = "Wrong answers",
                            value = state.wrongAnswers.toString(),
                            isDesktop = true,
                            modifier = Modifier.weight(1f)
                        )

                        ResultStatCard(
                            label = "Used time",
                            value = "${state.usedTimeSeconds}s",
                            isDesktop = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    ResultStatCard(
                        label = "Correct answers",
                        value = state.correctAnswers.toString(),
                        isDesktop = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ResultStatCard(
                        label = "Wrong answers",
                        value = state.wrongAnswers.toString(),
                        isDesktop = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    ResultStatCard(
                        label = "Used time",
                        value = "${state.usedTimeSeconds}s",
                        isDesktop = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(if (isDesktop) 6.dp else 2.dp))

                Button(
                    onClick = onBackToMoviesClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 46.dp)
                ) {
                    Text("Back to Movies")
                }
            }
        }
    }
}

@Composable
private fun ResultStatCard(
    label: String,
    value: String,
    isDesktop: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(if (isDesktop) 18.dp else 14.dp),
            verticalArrangement = Arrangement.spacedBy(if (isDesktop) 6.dp else 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = if (isDesktop) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    MaterialTheme.typography.titleLarge
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun resultMessage(
    score: Double
): String {
    return when {
        score >= 90.0 -> "Excellent result."
        score >= 70.0 -> "Very good result."
        score >= 50.0 -> "Good attempt."
        score > 0.0 -> "Keep practicing."
        else -> "No correct answers this time."
    }
}