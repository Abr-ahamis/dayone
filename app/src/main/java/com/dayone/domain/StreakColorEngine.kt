package com.dayone.domain

import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object StreakColorEngine {
    private val recoveryBase = listOf(
        0xFFE53935.toInt(),
        0xFFEF5350.toInt(),
        0xFFF4511E.toInt(),
        0xFFFB8C00.toInt(),
        0xFFFFA726.toInt(),
        0xFFC0CA33.toInt(),
        0xFFAED131.toInt()
    )
    private val greens = listOf(0xFF7CDB8A.toInt(), 0xFF2DD87B.toInt(), 0xFF00C853.toInt())
    private const val maxRecoverySteps = 6

    fun getStreakColor(days: Int, streakBreaks: Int): Int {
        if (days <= 0) return 0xFFFF1744.toInt()
        val path = buildRecoveryPath(streakBreaks)
        val index = days - 1
        if (index < path.size) return path[index]
        val t = min((days - path.size).toFloat() / 30f, 1f)
        return lerp(0xFF00C853.toInt(), 0xFF00E676.toInt(), t)
    }

    fun composeColor(days: Int, streakBreaks: Int): Color = Color(getStreakColor(days, streakBreaks))

    fun greenThreshold(streakBreaks: Int): Int = max(1, buildRecoveryPath(streakBreaks).size - 2)

    fun icon(days: Int): String = when {
        days <= 0 -> "💔"
        days < 3 -> "🌱"
        days < 7 -> "⚡"
        days < 14 -> "🔥"
        days < 30 -> "🏅"
        else -> "👑"
    }

    private fun buildRecoveryPath(breaks: Int): List<Int> {
        val extra = min(maxRecoverySteps, max(0, breaks))
        val warm = if (extra == 0) {
            recoveryBase
        } else {
            recoveryBase + List(extra) { i ->
                val t = if (extra == 1) 0.5f else i.toFloat() / (extra - 1)
                lerp(0xFFAED131.toInt(), 0xFF7CDB8A.toInt(), t)
            }
        }
        return warm + greens
    }

    private fun lerp(a: Int, b: Int, t: Float): Int {
        val ca = android.graphics.Color.valueOf(a)
        val cb = android.graphics.Color.valueOf(b)
        return android.graphics.Color.argb(
            1f,
            ca.red() + (cb.red() - ca.red()) * t,
            ca.green() + (cb.green() - ca.green()) * t,
            ca.blue() + (cb.blue() - ca.blue()) * t
        )
    }

    fun darken(color: Int, amount: Float): Int {
        val c = android.graphics.Color.valueOf(color)
        return android.graphics.Color.rgb(
            (c.red() * (1f - amount) * 255).roundToInt(),
            (c.green() * (1f - amount) * 255).roundToInt(),
            (c.blue() * (1f - amount) * 255).roundToInt()
        )
    }
}
