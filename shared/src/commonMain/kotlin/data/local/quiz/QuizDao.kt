package data.local.quiz

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_stats WHERE userId = :userId")
    fun observeStats(userId: Int): Flow<QuizStatsEntity?>

    @Query("SELECT * FROM quiz_stats WHERE userId = :userId")
    suspend fun getStats(userId: Int): QuizStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStats(stats: QuizStatsEntity)
}