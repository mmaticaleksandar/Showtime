package presentation.quiz.mvi

sealed interface QuizEffect {
    data object NavigateToResult : QuizEffect
    data object NavigateToMovies : QuizEffect
}