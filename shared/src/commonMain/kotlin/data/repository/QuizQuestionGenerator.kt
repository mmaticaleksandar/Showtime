package data.repository

import data.local.movie.MovieDao
import data.local.movie.MovieEntity
import domain.model.QuizQuestion
import domain.model.QuizQuestionType
import domain.model.QuizCategory

class QuizQuestionGenerator(
    private val movieDao: MovieDao,
    private val moviesRepository: MoviesRepository
) {
    suspend fun generateQuestions(
        category: QuizCategory
    ): List<QuizQuestion> {
        if (category != QuizCategory.MOVIE_KNOWLEDGE) {
            return emptyList()
        }
        moviesRepository.bootstrapQuizPool()

        val movies = movieDao
            .getQuizMoviePool()
            .filter {
                !it.posterUrl.isNullOrBlank() || !it.backdropUrl.isNullOrBlank()
            }

        if (movies.size < 10) {
            return emptyList()
        }

        val questionTypes = listOf(
            QuizQuestionType.GUESS_MOVIE,
            QuizQuestionType.GUESS_MOVIE,
            QuizQuestionType.GUESS_MOVIE,
            QuizQuestionType.GUESS_MOVIE,

            QuizQuestionType.GUESS_YEAR,
            QuizQuestionType.GUESS_YEAR,
            QuizQuestionType.GUESS_YEAR,

            QuizQuestionType.GUESS_LEAD_ACTOR,
            QuizQuestionType.GUESS_LEAD_ACTOR,
            QuizQuestionType.GUESS_LEAD_ACTOR
        ).shuffled()

        val questions = mutableListOf<QuizQuestion>()
        val usedImageUrls = mutableSetOf<String>()
        val usedMovieIds = mutableSetOf<String>()

        var questionId = 1
        var attempts = 0
        val maxAttempts = 250

        while (questions.size < 10 && attempts < maxAttempts) {
            attempts++

            val questionType = questionTypes[questions.size]

            val movie = movies
                .filter { candidate ->
                    val imageUrl = when (questionType) {
                        QuizQuestionType.GUESS_MOVIE ->
                            candidate.backdropUrl ?: candidate.posterUrl

                        QuizQuestionType.GUESS_YEAR,
                        QuizQuestionType.GUESS_LEAD_ACTOR ->
                            candidate.posterUrl
                    }

                    candidate.imdbId !in usedMovieIds &&
                            !imageUrl.isNullOrBlank() &&
                            imageUrl !in usedImageUrls
                }
                .shuffled()
                .firstOrNull()
                ?: break

            val question: QuizQuestion? = when (questionType) {
                QuizQuestionType.GUESS_MOVIE -> {
                    moviesRepository.ensureMovieDetail(movie.imdbId)

                    val detail = moviesRepository.getLocalMovieDetail(movie.imdbId)

                    createGuessMovieQuestion(
                        id = questionId,
                        correctMovie = movie,
                        backdropUrlFromDetail = detail?.backdropUrl,
                        allMovies = movies
                    )
                }

                QuizQuestionType.GUESS_YEAR -> createGuessYearQuestion(
                    id = questionId,
                    correctMovie = movie
                )

                QuizQuestionType.GUESS_LEAD_ACTOR -> {
                    moviesRepository.ensureMovieCast(movie.imdbId)

                    createGuessLeadActorQuestion(
                        id = questionId,
                        correctMovie = movie
                    )
                }
            }

            if (question != null && question.imageUrl !in usedImageUrls) {
                questions.add(question)
                usedMovieIds.add(movie.imdbId)

                question.imageUrl?.let { imageUrl ->
                    usedImageUrls.add(imageUrl)
                }

                questionId++
            } else {
                usedMovieIds.add(movie.imdbId)
            }
        }

        return questions
    }



    private fun createGuessMovieQuestion(
        id: Int,
        correctMovie: data.local.movie.MovieEntity,
        backdropUrlFromDetail: String?,
        allMovies: List<data.local.movie.MovieEntity>
    ): QuizQuestion? {
        val imageUrl = backdropUrlFromDetail
            ?: correctMovie.backdropUrl
            ?: correctMovie.posterUrl

        if (imageUrl.isNullOrBlank()) {
            return null
        }

        val wrongAnswers = allMovies
            .filter { it.imdbId != correctMovie.imdbId }
            .map { it.title }
            .filter { it.isNotBlank() }
            .distinct()
            .shuffled()
            .take(3)

        if (wrongAnswers.size < 3) {
            return null
        }

        val answers = (wrongAnswers + correctMovie.title)
            .shuffled()

        return QuizQuestion(
            id = id,
            type = QuizQuestionType.GUESS_MOVIE,
            imageUrl = imageUrl,
            title = null,
            answers = answers,
            correctAnswer = correctMovie.title
        )
    }

    private fun createGuessYearQuestion(
        id: Int,
        correctMovie: data.local.movie.MovieEntity
    ): QuizQuestion? {
        val year = correctMovie.year ?: return null

        if (correctMovie.posterUrl.isNullOrBlank()) {
            return null
        }

        val wrongYears = mutableSetOf<Int>()

        while (wrongYears.size < 3) {
            val offset = (-10..10)
                .filter { it != 0 }
                .random()

            wrongYears.add(year + offset)
        }

        val answers = (wrongYears.map { it.toString() } + year.toString())
            .shuffled()

        return QuizQuestion(
            id = id,
            type = QuizQuestionType.GUESS_YEAR,
            imageUrl = correctMovie.posterUrl,
            title = correctMovie.title,
            answers = answers,
            correctAnswer = year.toString()
        )
    }

    private suspend fun createGuessLeadActorQuestion(
        id: Int,
        correctMovie: data.local.movie.MovieEntity
    ): QuizQuestion? {
        val cast = movieDao
            .getCastForMovie(correctMovie.imdbId)
            .filter { castMember ->
                castMember.department == "Acting" &&
                        castMember.name.isNotBlank()
            }

        val topActors = cast.take(3)

        if (topActors.isEmpty()) {
            return null
        }

        val correctActor = topActors.random()

        val currentMovieActorNames = cast
            .map { castMember -> castMember.name }
            .toSet()

        val wrongActors = movieDao
            .getRandomActorsExcept(
                movieId = correctMovie.imdbId,
                actorName = correctActor.name
            )
            .map { actor -> actor.name }
            .filter { actorName ->
                actorName.isNotBlank()
            }
            .filter { actorName ->
                actorName !in currentMovieActorNames
            }
            .distinct()
            .shuffled()
            .take(3)

        if (wrongActors.size < 3) {
            return null
        }

        val answers = (wrongActors + correctActor.name)
            .shuffled()

        return QuizQuestion(
            id = id,
            type = QuizQuestionType.GUESS_LEAD_ACTOR,
            imageUrl = correctMovie.posterUrl,
            title = correctMovie.title,
            answers = answers,
            correctAnswer = correctActor.name
        )
    }
}