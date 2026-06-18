package presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun QuizHeader(
    title: String,
    subtitle: String,
    timeLeftSeconds: Int,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    isDesktop: Boolean,
    onAbandonClick: () -> Unit
) {
    val safeTotal = max(totalQuestions, 1)
    val questionProgress = currentQuestionNumber.toFloat() / safeTotal.toFloat()
    val timeProgress = timeLeftSeconds.coerceIn(0, 60) / 60f

    if (isDesktop) {
        DesktopQuizHeader(
            title = title,
            subtitle = subtitle,
            timeLeftSeconds = timeLeftSeconds,
            currentQuestionNumber = currentQuestionNumber,
            totalQuestions = totalQuestions,
            questionProgress = questionProgress,
            timeProgress = timeProgress,
            onAbandonClick = onAbandonClick
        )
    } else {
        MobileQuizHeader(
            title = title,
            timeLeftSeconds = timeLeftSeconds,
            currentQuestionNumber = currentQuestionNumber,
            totalQuestions = totalQuestions,
            timeProgress = timeProgress,
            onAbandonClick = onAbandonClick
        )
    }
}

@Composable
private fun DesktopQuizHeader(
    title: String,
    subtitle: String,
    timeLeftSeconds: Int,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    questionProgress: Float,
    timeProgress: Float,
    onAbandonClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = onAbandonClick
            ) {
                Text("Abandon")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question $currentQuestionNumber/$totalQuestions",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Time left: ${timeLeftSeconds}s",
                style = MaterialTheme.typography.bodyMedium,
                color = timerColor(timeLeftSeconds)
            )
        }

        LinearProgressIndicator(
            progress = {
                timeProgress
            },
            modifier = Modifier.fillMaxWidth()
        )

        LinearProgressIndicator(
            progress = {
                questionProgress.coerceIn(0f, 1f)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MobileQuizHeader(
    title: String,
    timeLeftSeconds: Int,
    currentQuestionNumber: Int,
    totalQuestions: Int,
    timeProgress: Float,
    onAbandonClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )

//            OutlinedButton(
//                onClick = onAbandonClick
//            ) {
//                Text("Abandon")
//            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Question $currentQuestionNumber/$totalQuestions",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${timeLeftSeconds}s left",
                style = MaterialTheme.typography.bodyMedium,
                color = timerColor(timeLeftSeconds)
            )
        }

        LinearProgressIndicator(
            progress = {
                timeProgress
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun timerColor(
    timeLeftSeconds: Int
) = if (timeLeftSeconds <= 10) {
    MaterialTheme.colorScheme.error
} else {
    MaterialTheme.colorScheme.onSurfaceVariant
}