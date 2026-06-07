package com.dayone.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DayOneDao {
    @Query("SELECT * FROM streak_state WHERE id = 1")
    fun observeState(): Flow<StreakStateEntity?>

    @Query("SELECT * FROM streak_state WHERE id = 1")
    suspend fun getState(): StreakStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertState(state: StreakStateEntity)

    @Query("SELECT * FROM calendar_history WHERE epochDay BETWEEN :start AND :end ORDER BY epochDay ASC")
    fun observeHistory(start: Long, end: Long): Flow<List<CalendarEntryEntity>>

    @Query("SELECT * FROM calendar_history WHERE epochDay BETWEEN :start AND :end ORDER BY epochDay ASC")
    suspend fun getHistory(start: Long, end: Long): List<CalendarEntryEntity>

    @Query("SELECT * FROM calendar_history ORDER BY epochDay ASC")
    suspend fun getAllHistory(): List<CalendarEntryEntity>

    @Query("SELECT * FROM calendar_history WHERE epochDay = :epochDay")
    suspend fun getEntry(epochDay: Long): CalendarEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEntry(entry: CalendarEntryEntity)

    @Query("DELETE FROM calendar_history")
    suspend fun clearHistory()
}
