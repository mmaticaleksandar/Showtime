package domain.model

enum class QuizQuestionType {
    GUESS_MOVIE,
    GUESS_YEAR,
    GUESS_LEAD_ACTOR
}

data class QuizQuestion(
    val id: Int,
    val type: QuizQuestionType,
    val imageUrl: String?,
    val title: String?,
    val answers: List<String>,
    val correctAnswer: String
)