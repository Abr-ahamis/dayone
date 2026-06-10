package com.dayone.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import com.dayone.R
import java.time.LocalDate

object ContribCalendarBuilder {
    fun buildBitmap(context: Context, history: List<DayHistory>, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val cellGap = 3f * context.resources.displayMetrics.density
        val columns = 16
        val rows = 7
        
        val cellWidth = (width - (columns - 1) * cellGap) / columns
        val cellHeight = (height - (rows - 1) * cellGap) / rows
        val cellSize = minOf(cellWidth, cellHeight)
        
        val totalGridWidth = columns * cellSize + (columns - 1) * cellGap
        val startX = (width - totalGridWidth) / 2
        val startY = (height - (rows * cellSize + (rows - 1) * cellGap)) / 2

        val today = LocalDate.now()
        val historyMap = history.associateBy { it.date }

        for (col in 0 until columns) {
            for (row in 0 until rows) {
                val dayOffset = (columns - 1 - col) * 7 + (6 - row)
                val date = today.minusDays(dayOffset.toLong())
                val entry = historyMap[date]
                
                val left = startX + col * (cellSize + cellGap)
                val top = startY + row * (cellSize + cellGap)
                val rect = RectF(left, top, left + cellSize, top + cellSize)
                
                paint.color = when {
                    date.isAfter(today) -> ContextCompat.getColor(context, R.color.bg_glass2)
                    entry?.isCompleted == true -> StreakColorEngine.getStreakColor(entry.streak)
                    entry?.isMissed == true -> ContextCompat.getColor(context, R.color.danger)
                    else -> ContextCompat.getColor(context, R.color.bg_elevated)
                }
                
                canvas.drawRoundRect(rect, 4f * context.resources.displayMetrics.density, 4f * context.resources.displayMetrics.density, paint)
                
                if (date == today) {
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 2f * context.resources.displayMetrics.density
                    paint.color = ContextCompat.getColor(context, R.color.accent_blue)
                    canvas.drawRoundRect(rect, 4f * context.resources.displayMetrics.density, 4f * context.resources.displayMetrics.density, paint)
                    paint.style = Paint.Style.FILL
                }
            }
        }

        return bitmap
    }
}

data class DayHistory(
    val date: LocalDate,
    val isCompleted: Boolean,
    val isMissed: Boolean,
    val streak: Int
)
