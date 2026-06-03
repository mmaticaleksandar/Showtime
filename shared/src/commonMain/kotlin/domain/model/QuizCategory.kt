package domain.model

enum class QuizCategory(
    val id: Int,
    val title: String,
    val description: String,
    val isAvailable: Boolean
) {
    MOVIE_KNOWLEDGE(
        id = 1,
        title = "Movie Knowledge",
        description = "Guess movies, release years and lead actors.",
        isAvailable = true
    ),

    ACTORS(
        id = 2,
        title = "Actors",
        description = "Coming soon.",
        isAvailable = false
    ),

    GENRES(
        id = 3,
        title = "Genres",
        description = "Coming soon.",
        isAvailable = false
    )
}