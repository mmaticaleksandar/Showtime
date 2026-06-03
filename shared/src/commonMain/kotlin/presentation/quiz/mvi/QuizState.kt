package presentation.quiz.mvi

import domain.model.QuizQuestion
import domain.model.QuizCategory

data class QuizState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFinished: Boolean = false,
    val timeLeftSeconds: Int = 60,
    val usedTimeSeconds: Int = 0,
    val score: Double = 0.0,
    val showAbandonDialog: Boolean = false,
    val selectedCategory: QuizCategory? = null
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val currentQuestionNumber: Int
        get() = currentQuestionIndex + 1

    val totalQuestions: Int
        get() = questions.size
}