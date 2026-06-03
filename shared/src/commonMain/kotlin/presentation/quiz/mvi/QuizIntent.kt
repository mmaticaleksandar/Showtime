package presentation.quiz.mvi

import domain.model.QuizCategory

sealed interface QuizIntent {
    data class StartQuiz(
        val category: QuizCategory
    ) : QuizIntent
    data class AnswerSelected(val answer: String) : QuizIntent
    data object NextQuestion : QuizIntent

    data object BackClicked : QuizIntent
    data object DismissAbandonDialog : QuizIntent
    data object ConfirmAbandonQuiz : QuizIntent
}