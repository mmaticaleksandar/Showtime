package presentation.quiz.mvi

import domain.model.QuizQuestion
import domain.model.QuizCategory

sealed interface QuizChange {

    data class LoadingStarted(
        val category: QuizCategory
    ) : QuizChange

    data class QuestionsLoaded(
        val questions: List<QuizQuestion>
    ) : QuizChange

    data class LoadingFailed(
        val message: String
    ) : QuizChange

    data object AbandonDialogShown : QuizChange

    data object AbandonDialogDismissed : QuizChange

    data class TimeChanged(
        val timeLeftSeconds: Int
    ) : QuizChange

    data class AnswerSelected(
        val answer: String,
        val isCorrect: Boolean
    ) : QuizChange

    data object NextQuestion : QuizChange

    data class QuizFinished(
        val score: Double,
        val wrongAnswers: Int,
        val usedTimeSeconds: Int
    ) : QuizChange

    data object QuizAbandoned : QuizChange
}