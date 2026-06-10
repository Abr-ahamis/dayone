package com.dayone.engine

import android.graphics.Color

object StreakColorEngine {
    private val streakColors = listOf(
        "#ff1744", // 0
        "#e53935", // 1
        "#ef5350", // 2
        "#f4511e", // 3
        "#f4511e", // 4
        "#fb8c00", // 5
        "#ffa726", // 6
        "#ffa726", // 7
        "#c0ca33", // 8
        "#c0ca33", // 9
        "#aed131", // 10
        "#43a047", // 11
        "#2dd87b", // 12
        "#00c853"  // 13+
    )

    fun getStreakColor(currentStreak: Int, streakBreaks: Int = 0): Int {
        val baseGreenThreshold = 13
        val greenThreshold = baseGreenThreshold + streakBreaks
        
        return if (currentStreak >= greenThreshold) {
            Color.parseColor(streakColors.last())
        } else if (currentStreak < streakColors.size) {
            Color.parseColor(streakColors[currentStreak])
        } else {
            Color.parseColor(streakColors.last())
        }
    }

    fun getDayColor(streak: Int, isCompleted: Boolean, isMissed: Boolean): Int {
        return when {
            isMissed -> Color.parseColor("#ff1744")
            isCompleted -> getStreakColor(streak)
            else -> Color.parseColor("#162040") // Background Elevated
        }
    }

    fun buildRecoveryPath(streakBreaks: Int): List<Int> {
        val path = mutableListOf<Int>()
        val baseGreenThreshold = 13
        val greenThreshold = baseGreenThreshold + streakBreaks
        for (i in 0..greenThreshold) {
            path.add(getStreakColor(i, streakBreaks))
        }
        return path
    }
}
