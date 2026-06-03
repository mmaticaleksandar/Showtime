package presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun QuizAnswerOption(
    index: Int,
    answer: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    hasAnswered: Boolean,
    onClick: () -> Unit
) {
    val optionLabel = when (index) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        else -> "${index + 1}"
    }

    val containerColor = when {
        hasAnswered && isCorrect -> MaterialTheme.colorScheme.primaryContainer
        hasAnswered && isSelected -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        hasAnswered && isCorrect -> MaterialTheme.colorScheme.onPrimaryContainer
        hasAnswered && isSelected -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    ElevatedCard(
        onClick = {
            if (!hasAnswered) {
                onClick()
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = optionLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = answer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (hasAnswered && isCorrect) {
                Text("✓")
            } else if (hasAnswered && isSelected) {
                Text("✕")
            }
        }
    }
}