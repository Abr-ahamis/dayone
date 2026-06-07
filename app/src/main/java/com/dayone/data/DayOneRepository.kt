package com.dayone.data

import com.dayone.domain.BibleVerses
import com.dayone.domain.nowMillis
import com.dayone.domain.todayEpochDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class DayOneSnapshot(
    val state: StreakStateEntity,
    val history: List<CalendarEntryEntity>,
    val verse: String
)

@Singleton
class DayOneRepository @Inject constructor(
    private val dao: DayOneDao
) {
    fun observeSnapshot(start: Long, end: Long): Flow<DayOneSnapshot> =
        dao.observeState().map { ensureSeeded(it) }.map { state ->
            val history = dao.getHistory(start, end)
            DayOneSnapshot(state, history, BibleVerses.forIndex(state.verseIndex))
        }

    suspend fun snapshot(start: Long, end: Long): DayOneSnapshot {
        val state = ensureSeeded(dao.getState())
        return DayOneSnapshot(state, dao.getHistory(start, end), BibleVerses.forIndex(state.verseIndex))
    }

    suspend fun currentState(): StreakStateEntity = ensureSeeded(dao.getState())

    suspend fun completeToday() {
        val today = todayEpochDay()
        val state = ensureSeeded(dao.getState())
        val updatedStreak = (today - state.startEpochDay + 1).toInt().coerceAtLeast(1)
        dao.upsertEntry(CalendarEntryEntity(today, DayState.COMPLETED, updatedStreak, nowMillis()))
        dao.upsertState(
            state.copy(
                currentStreak = updatedStreak,
                longestStreak = maxOf(state.longestStreak, updatedStreak),
                totalCompleted = maxOf(state.totalCompleted, updatedStreak),
                lastUpdateEpochMillis = nowMillis()
            )
        )
    }

    suspend fun resetStreak() {
        val today = todayEpochDay()
        val state = ensureSeeded(dao.getState())
        dao.upsertEntry(CalendarEntryEntity(today, DayState.MISSED, 0, nowMillis()))
        dao.upsertState(
            state.copy(
                startEpochDay = today,
                currentStreak = 0,
                streakBreaks = state.streakBreaks + 1,
                lastUpdateEpochMillis = nowMillis()
            )
        )
    }

    suspend fun runMorningCycle(): MorningResult {
        val today = todayEpochDay()
        val state = ensureSeeded(dao.getState())
        val verse = BibleVerses.forIndex(state.verseIndex)
        if (state.lastMorningEpochDay != today) {
            dao.upsertState(
                state.copy(
                    verseIndex = BibleVerses.nextIndex(state.verseIndex),
                    lastMorningEpochDay = today,
                    lastUpdateEpochMillis = nowMillis()
                )
            )
        }
        return MorningResult(state.currentStreak, state.streakBreaks, verse)
    }

    suspend fun runNoonCycle() {
        val today = todayEpochDay()
        val state = ensureSeeded(dao.getState())
        if (state.lastNoonUpdateEpochDay == today) return

        val yesterday = today - 1
        val yesterdayEntry = dao.getEntry(yesterday)
        val newState = if (yesterdayEntry == null && yesterday >= state.startEpochDay) {
            dao.upsertEntry(CalendarEntryEntity(yesterday, DayState.MISSED, 0, nowMillis()))
            state.copy(
                startEpochDay = today,
                currentStreak = 0,
                streakBreaks = state.streakBreaks + 1,
                lastNoonUpdateEpochDay = today,
                lastUpdateEpochMillis = nowMillis()
            )
        } else {
            val current = (today - state.startEpochDay).toInt().coerceAtLeast(0)
            state.copy(
                currentStreak = current,
                longestStreak = maxOf(state.longestStreak, current),
                lastNoonUpdateEpochDay = today,
                lastUpdateEpochMillis = nowMillis()
            )
        }
        dao.upsertState(newState)
    }

    suspend fun clearAll() {
        dao.clearHistory()
        dao.upsertState(seedState())
    }

    private suspend fun ensureSeeded(existing: StreakStateEntity?): StreakStateEntity {
        if (existing != null) return existing
        val seeded = seedState()
        dao.upsertState(seeded)
        seedHistory(seeded)
        return seeded.copy(currentStreak = 12, longestStreak = 21, totalCompleted = 87)
    }

    private fun seedState(): StreakStateEntity {
        val today = todayEpochDay()
        return StreakStateEntity(
            startEpochDay = today - 11,
            currentStreak = 12,
            longestStreak = 21,
            totalCompleted = 87,
            lastUpdateEpochMillis = nowMillis()
        )
    }

    private suspend fun seedHistory(state: StreakStateEntity) {
        val today = todayEpochDay()
        for (i in 0..83) {
            val day = today - (83 - i)
            val streak = if (day >= state.startEpochDay) (day - state.startEpochDay + 1).toInt() else (i % 10) + 1
            val missed = i == 55 || i == 64
            dao.upsertEntry(
                CalendarEntryEntity(
                    epochDay = day,
                    state = if (missed) DayState.MISSED else DayState.COMPLETED,
                    streakValue = if (missed) 0 else streak,
                    updatedAtEpochMillis = nowMillis()
                )
            )
        }
    }
}

data class MorningResult(
    val streak: Int,
    val streakBreaks: Int,
    val verse: String
)
