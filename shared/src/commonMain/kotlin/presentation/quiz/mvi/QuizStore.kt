package presentation.quiz.mvi

import data.repository.QuizQuestionGenerator
import data.repository.QuizRepository
import domain.model.QuizCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import data.repository.AuthRepository

class QuizStore(
    private val quizQuestionGenerator: QuizQuestionGenerator,
    private val quizRepository: QuizRepository,
    private val authRepository: AuthRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var timerStarted = false
    private var timerJob: Job? = null

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<QuizEffect>()
    val effect: SharedFlow<QuizEffect> = _effect.asSharedFlow()

    private fun reduce(change: QuizChange) {
        _state.value = QuizReducer.reduce(
            state = _state.value,
            change = change
        )
    }

    fun onIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.StartQuiz -> startQuiz(intent.category)

            is QuizIntent.AnswerSelected -> {
                selectAnswer(intent.answer)
            }

            QuizIntent.NextQuestion -> {
                nextQuestion()
            }

            QuizIntent.BackClicked -> {
                reduce(QuizChange.AbandonDialogShown)
            }

            QuizIntent.DismissAbandonDialog -> {
                reduce(QuizChange.AbandonDialogDismissed)
            }

            QuizIntent.ConfirmAbandonQuiz -> {
                abandonQuiz()
            }
        }
    }

    private fun startQuiz(category : QuizCategory) {
        timerStarted = false
        timerJob?.cancel()
        timerJob = null

        reduce(
            QuizChange.LoadingStarted(
                category = category
            )
        )

        scope.launch {
            val questions = quizQuestionGenerator.generateQuestions(
                category = category
            )

            if (questions.size < 10) {
                reduce(
                    QuizChange.LoadingFailed(
                        message = "Browse the catalog first to populate your quiz pool."
                    )
                )
                return@launch
            }

            reduce(
                QuizChange.QuestionsLoaded(
                    questions = questions
                )
            )

            startTimer()
        }
    }

    private fun startTimer() {
        if (timerStarted) return

        timerStarted = true

        timerJob?.cancel()

        timerJob = scope.launch {
            while (
                _state.value.timeLeftSeconds > 0 &&
                !_state.value.isFinished
            ) {
                delay(1000)

                val state = _state.value

                if (!state.isFinished) {
                    reduce(
                        QuizChange.TimeChanged(
                            timeLeftSeconds = state.timeLeftSeconds - 1
                        )
                    )
                }
            }

            if (!_state.value.isFinished) {
                finishQuiz()
            }
        }
    }

    private fun selectAnswer(answer: String) {
        val state = _state.value

        if (state.selectedAnswer != null) {
            return
        }

        if (state.isFinished) {
            return
        }

        val currentQuestion = state.currentQuestion ?: return
        val isCorrect = answer == currentQuestion.correctAnswer

        reduce(
            QuizChange.AnswerSelected(
                answer = answer,
                isCorrect = isCorrect
            )
        )

        scope.launch {
            delay(900)

            if (_state.value.isFinished) {
                return@launch
            }

            nextQuestion()
        }
    }

    private fun nextQuestion() {
        val state = _state.value

        if (state.currentQuestionIndex == state.questions.lastIndex) {
            finishQuiz()
            return
        }

        reduce(QuizChange.NextQuestion)
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        timerJob = null
        timerStarted = false

        val state = _state.value

        if (state.isFinished) return

        val bto = state.correctAnswers
        val mvt = 60.0
        val pvt = state.timeLeftSeconds.toDouble()

        val finalScore = (bto * (9.0 + pvt / mvt))
            .coerceAtMost(100.0)

        val totalQuestions = state.questions.size
        val finalWrongAnswers = totalQuestions - state.correctAnswers
        val usedTimeSeconds = 60 - state.timeLeftSeconds

        reduce(
            QuizChange.QuizFinished(
                score = finalScore,
                wrongAnswers = finalWrongAnswers,
                usedTimeSeconds = usedTimeSeconds
            )
        )

        scope.launch {
            val userId = authRepository.getCurrentUserId()

            if (userId != null) {
                quizRepository.saveQuizResult(
                    userId = userId,
                    score = finalScore
                )
            }

            _effect.emit(QuizEffect.NavigateToResult)
        }
    }

    private fun abandonQuiz() {
        timerJob?.cancel()
        timerJob = null
        timerStarted = false

        reduce(QuizChange.QuizAbandoned)

        scope.launch {
            _effect.emit(QuizEffect.NavigateToMovies)
        }
    }
}