package com.dayone.data

import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DayOneRepository @Inject constructor(
    private val dao: DayOneDao
) {
    suspend fun currentState(): StreakStateEntity = dao.getState() ?: seedState()

    suspend fun getHistory(): List<com.dayone.engine.DayHistory> {
        return dao.getAllHistory().map {
            com.dayone.engine.DayHistory(
                date = java.time.LocalDate.ofEpochDay(it.epochDay),
                isCompleted = it.state == DayState.COMPLETED,
                isMissed = it.state == DayState.MISSED,
                streak = it.streakValue
            )
        }
    }

    suspend fun syncFromWebView(json: String) {
        val obj = JSONObject(json)
        val state = StreakStateEntity(
            habitName = obj.optString("habitName", "No Habit Yet"),
            emoji = obj.optString("emoji", "🎯"),
            currentStreak = obj.optInt("currentStreak", 0),
            colorHex = obj.optString("color", "#4F8EF7"),
            status = obj.optString("status", "On Track"),
            lastUpdateEpochMillis = obj.optLong("updatedAt", System.currentTimeMillis())
        )
        dao.upsertState(state)
    }

    private fun seedState(): StreakStateEntity {
        return StreakStateEntity(
            lastUpdateEpochMillis = System.currentTimeMillis()
        )
    }

    suspend fun clearAll() {
        dao.clearHistory()
        dao.upsertState(seedState())
    }
}
