package data.local.quiz

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_stats")
data class QuizStatsEntity(
    @PrimaryKey
    val userId: Int,
    val bestScore: Double = 0.0,
    val playedCount: Int = 0
)