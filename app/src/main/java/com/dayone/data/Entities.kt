package com.dayone.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streak_state")
data class StreakStateEntity(
    @PrimaryKey val id: Int = 1,
    val habitName: String = "No Sugar",
    val emoji: String = "🔥",
    val colorHex: String = "#4F8EF7",
    val startEpochDay: Long,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalCompleted: Int = 0,
    val streakBreaks: Int = 0,
    val verseIndex: Int = 1,
    val lastUpdateEpochMillis: Long = 0L,
    val lastNoonUpdateEpochDay: Long = 0L,
    val lastMorningEpochDay: Long = 0L
)

@Entity(tableName = "calendar_history")
data class CalendarEntryEntity(
    @PrimaryKey val epochDay: Long,
    val state: DayState,
    val streakValue: Int,
    val updatedAtEpochMillis: Long
)

enum class DayState {
    COMPLETED,
    MISSED,
    FUTURE,
    EMPTY
}
