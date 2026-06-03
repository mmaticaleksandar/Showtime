package presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import domain.model.QuizQuestion
import domain.model.QuizQuestionType
import presentation.quiz.mvi.QuizIntent
import presentation.quiz.mvi.QuizState

@Composable
fun QuizQuestionContent(
    state: QuizState,
    isDesktop: Boolean,
    onIntent: (QuizIntent) -> Unit
) {
    val question = state.currentQuestion ?: return

    if (isDesktop) {
        DesktopQuizQuestionContent(
            question = question,
            selectedAnswer = state.selectedAnswer,
            onIntent = onIntent
        )
    } else {
        MobileQuizQuestionContent(
            question = question,
            selectedAnswer = state.selectedAnswer,
            onIntent = onIntent
        )
    }
}

@Composable
private fun DesktopQuizQuestionContent(
    question: QuizQuestion,
    selectedAnswer: String?,
    onIntent: (QuizIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        QuizImageCard(
            question = question,
            isDesktop = true,
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
        )

        ElevatedCard(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxHeight()
        ) {
            QuizQuestionAndAnswers(
                question = question,
                selectedAnswer = selectedAnswer,
                isDesktop = true,
                onIntent = onIntent,
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            )
        }
    }
}

@Composable
private fun MobileQuizQuestionContent(
    question: QuizQuestion,
    selectedAnswer: String?,
    onIntent: (QuizIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuizImageCard(
            question = question,
            isDesktop = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp)
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            QuizQuestionAndAnswers(
                question = question,
                selectedAnswer = selectedAnswer,
                isDesktop = false,
                onIntent = onIntent,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun QuizImageCard(
    question: QuizQuestion,
    isDesktop: Boolean,
    modifier: Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        if (question.imageUrl != null) {
            AsyncImage(
                model = question.imageUrl,
                contentDescription = question.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = if (isDesktop) {
                            220.dp
                        } else {
                            180.dp
                        }
                    )
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                modifier = Modifier.padding(
                    if (isDesktop) {
                        18.dp
                    } else {
                        12.dp
                    }
                )
            ) {
                Text(
                    text = question.title ?: "Movie question",
                    style = if (isDesktop) {
                        MaterialTheme.typography.headlineSmall
                    } else {
                        MaterialTheme.typography.titleLarge
                    },
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "No image available for this question.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QuizQuestionAndAnswers(
    question: QuizQuestion,
    selectedAnswer: String?,
    isDesktop: Boolean,
    onIntent: (QuizIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            if (isDesktop) {
                14.dp
            } else {
                10.dp
            }
        )
    ) {
        Text(
            text = questionTitle(question),
            style = if (isDesktop) {
                MaterialTheme.typography.headlineSmall
            } else {
                MaterialTheme.typography.titleLarge
            },
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = questionDescription(question),
            style = if (isDesktop) {
                MaterialTheme.typography.bodyLarge
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            Modifier.height(
                if (isDesktop) {
                    4.dp
                } else {
                    0.dp
                }
            )
        )

        question.answers.forEachIndexed { index, answer ->
            QuizAnswerOption(
                index = index,
                answer = answer,
                isSelected = selectedAnswer == answer,
                isCorrect = answer == question.correctAnswer,
                hasAnswered = selectedAnswer != null,
                onClick = {
                    onIntent(QuizIntent.AnswerSelected(answer))
                }
            )
        }
    }
}

private fun questionTitle(
    question: QuizQuestion
): String {
    return when (question.type) {
        QuizQuestionType.GUESS_MOVIE -> "Guess the movie"
        QuizQuestionType.GUESS_YEAR -> "Guess the year"
        QuizQuestionType.GUESS_LEAD_ACTOR -> "Guess the lead actor"
    }
}

private fun questionDescription(
    question: QuizQuestion
): String {
    return when (question.type) {
        QuizQuestionType.GUESS_MOVIE ->
            "Which movie is shown in the image?"

        QuizQuestionType.GUESS_YEAR ->
            "Which year was \"${question.title}\" released?"

        QuizQuestionType.GUESS_LEAD_ACTOR ->
            "Who is one of the lead actors in \"${question.title}\"?"
    }
}