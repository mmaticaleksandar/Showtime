package data.repository

import data.local.quiz.QuizDao
import data.local.quiz.QuizStatsEntity
import domain.model.QuizStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuizRepository(
    private val quizDao: QuizDao
) {
    fun observeStats(userId: Int): Flow<QuizStats> {
        return quizDao.observeStats(userId).map { entity ->
            QuizStats(
                bestScore = entity?.bestScore ?: 0.0,
                playedCount = entity?.playedCount ?: 0
            )
        }
    }

    suspend fun saveQuizResult(
        userId: Int,
        score: Double
    ) {
        val currentStats = quizDao.getStats(userId)

        val newBestScore = maxOf(
            currentStats?.bestScore ?: 0.0,
            score
        )

        val newPlayedCount =
            (currentStats?.playedCount ?: 0) + 1

        quizDao.upsertStats(
            QuizStatsEntity(
                userId = userId,
                bestScore = newBestScore,
                playedCount = newPlayedCount
            )
        )
    }
}