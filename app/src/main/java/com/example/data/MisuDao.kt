package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MisuDao {
    @Query("SELECT * FROM misu_state WHERE id = 1 LIMIT 1")
    fun getMisuState(): Flow<MisuEntity?>

    @Query("SELECT * FROM misu_state WHERE id = 1 LIMIT 1")
    suspend fun getMisuStateSync(): MisuEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMisuState(state: MisuEntity)

    @Insert
    suspend fun insertMoodLog(log: MoodLogEntity)

    @Query("SELECT * FROM mood_logs ORDER BY timestamp DESC")
    fun getAllMoodLogs(): Flow<List<MoodLogEntity>>

    @Query("DELETE FROM mood_logs")
    suspend fun clearHistory()
}
