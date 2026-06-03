package presentation.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.quiz.mvi.QuizIntent
import presentation.quiz.mvi.QuizState


@Composable
fun QuizScreen(
    state: QuizState,
    onIntent: (QuizIntent) -> Unit,
    onBackClick: () -> Unit
) {
    val question = state.currentQuestion



    if (state.showAbandonDialog) {
        AlertDialog(
            onDismissRequest = {
                onIntent(QuizIntent.DismissAbandonDialog)
            },
            title = {
                Text("Abandon Quiz")
            },
            text = {
                Text("Are you sure you want to abandon the quiz? Your progress will be lost.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onIntent(QuizIntent.ConfirmAbandonQuiz)
                    }
                ) {
                    Text("Abandon")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        onIntent(QuizIntent.DismissAbandonDialog)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isDesktop = maxWidth >= 900.dp
        val contentMaxWidth = if (isDesktop) 1100.dp else maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp)
                .padding(top = if (isDesktop) 20.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
            ) {
                QuizHeader(
                    title = "Quiz",
                    subtitle = state.selectedCategory?.title ?: "Movie Knowledge",
                    timeLeftSeconds = state.timeLeftSeconds,
                    currentQuestionNumber = state.currentQuestionNumber,
                    totalQuestions = state.totalQuestions,
                    isDesktop = isDesktop,
                    onAbandonClick = {
                        onIntent(QuizIntent.BackClicked)
                    }
                )

                Spacer(Modifier.height(if (isDesktop) 16.dp else 10.dp))
            }

            Box(
                modifier = Modifier
                    .widthIn(max = contentMaxWidth)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    state.isLoading -> {
                        QuizLoadingState()
                    }

                    state.errorMessage != null -> {
                        QuizErrorState(
                            message = state.errorMessage,
                            onBackClick = onBackClick
                        )
                    }

                    question == null -> {
                        QuizEmptyState()
                    }

                    else -> {
                        AnimatedContent(
                            targetState = state.currentQuestionIndex,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "quiz_question_transition"
                        ) { targetQuestionIndex ->
                            QuizQuestionContent(
                                state = state.copy(
                                    currentQuestionIndex = targetQuestionIndex
                                ),
                                isDesktop = isDesktop,
                                onIntent = onIntent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Preparing quiz questions...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun QuizErrorState(
    message: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Quiz cannot start",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onBackClick
                ) {
                    Text("Back to movies")
                }
            }
        }
    }
}

@Composable
private fun QuizEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No quiz question available.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}