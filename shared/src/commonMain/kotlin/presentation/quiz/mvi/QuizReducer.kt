package presentation.quiz.mvi

object QuizReducer {

    fun reduce(
        state: QuizState,
        change: QuizChange
    ): QuizState {
        return when (change) {
            is QuizChange.LoadingStarted -> QuizState(
                selectedCategory = change.category,
                isLoading = true
            )

            is QuizChange.QuestionsLoaded -> QuizState(
                selectedCategory = state.selectedCategory,
                questions = change.questions,
                isLoading = false,
                timeLeftSeconds = 60
            )

            is QuizChange.LoadingFailed -> QuizState(
                isLoading = false,
                errorMessage = change.message
            )

            QuizChange.AbandonDialogShown -> state.copy(
                showAbandonDialog = true
            )

            QuizChange.AbandonDialogDismissed -> state.copy(
                showAbandonDialog = false
            )

            is QuizChange.TimeChanged -> state.copy(
                timeLeftSeconds = change.timeLeftSeconds
            )

            is QuizChange.AnswerSelected -> state.copy(
                selectedAnswer = change.answer,
                correctAnswers = if (change.isCorrect) {
                    state.correctAnswers + 1
                } else {
                    state.correctAnswers
                },
                wrongAnswers = if (change.isCorrect) {
                    state.wrongAnswers
                } else {
                    state.wrongAnswers + 1
                }
            )

            QuizChange.NextQuestion -> state.copy(
                currentQuestionIndex = state.currentQuestionIndex + 1,
                selectedAnswer = null
            )

            is QuizChange.QuizFinished -> state.copy(
                isFinished = true,
                score = change.score,
                wrongAnswers = change.wrongAnswers,
                usedTimeSeconds = change.usedTimeSeconds,
                selectedAnswer = null,
                showAbandonDialog = false
            )

            QuizChange.QuizAbandoned -> QuizState()
        }
    }
}