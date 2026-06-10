package com.dayone.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import com.dayone.MainActivity
import com.dayone.R
import com.dayone.data.DayOneRepository
import com.dayone.engine.ContribCalendarBuilder
import com.dayone.engine.StreakColorEngine
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DayOneWidgetUpdater @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DayOneRepository,
    private val preferencesStore: com.dayone.data.PreferencesStore
) {
    suspend fun updateAllWidgets() {
        android.util.Log.d("DayOneWidget", "Updating all widgets")
        val manager = AppWidgetManager.getInstance(context)
        updateCompactWidgets(manager)
        updateExpandedWidgets(manager)
        
        val state = repository.currentState()
        val prefs = preferencesStore.preferences.first()
        android.util.Log.d("DayOneWidget", "Current streak: ${state.currentStreak}, Dynamic icon enabled: ${prefs.dynamicIcon}")
        if (prefs.dynamicIcon) {
            com.dayone.engine.IconManager.updateIcon(context, state.currentStreak)
        }
    }

    private suspend fun updateCompactWidgets(manager: AppWidgetManager) {
        val component = ComponentName(context, CompactDayOneWidgetProvider::class.java)
        val ids = manager.getAppWidgetIds(component)
        val state = repository.currentState()
        val color = StreakColorEngine.getStreakColor(state.currentStreak)

        ids.forEach { id ->
            val options = manager.getAppWidgetOptions(id)
            val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

            val isSmall = minWidth < 150 || minHeight < 150
            val layoutId = if (isSmall) R.layout.widget_compact_small else R.layout.widget_compact_medium
            
            val views = RemoteViews(context.packageName, layoutId)
            views.setInt(R.id.widgetRoot, "setBackgroundColor", color)
            views.setTextViewText(R.id.streakCount, state.currentStreak.toString())
            views.setTextViewText(R.id.flameIcon, state.emoji)
            
            views.setOnClickPendingIntent(R.id.widgetRoot, launchIntent(id))
            manager.updateAppWidget(id, views)
        }
    }

    private suspend fun updateExpandedWidgets(manager: AppWidgetManager) {
        val component = ComponentName(context, ExpandedDayOneWidgetProvider::class.java)
        val ids = manager.getAppWidgetIds(component)
        val state = repository.currentState()
        val history = repository.getHistory()
        val color = StreakColorEngine.getStreakColor(state.currentStreak)

        ids.forEach { id ->
            val options = manager.getAppWidgetOptions(id)
            val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

            val is5x2 = minHeight < 170
            val layoutId = if (is5x2) R.layout.widget_expanded_5x2 else R.layout.widget_expanded_5x3
            
            val views = RemoteViews(context.packageName, layoutId)
            views.setTextViewText(R.id.streakCount, state.currentStreak.toString())
            views.setTextColor(R.id.streakCount, color)
            views.setTextViewText(R.id.activityStreak, state.currentStreak.toString())
            views.setTextViewText(R.id.habitName, state.habitName)
            views.setTextViewText(R.id.statusText, state.status)
            views.setInt(R.id.statusDot, "setBackgroundColor", color)

            if (!is5x2) {
                val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE dd MMM")).uppercase()
                views.setTextViewText(R.id.dateLabel, dateStr)
            }

            // Contribution Grid
            val density = context.resources.displayMetrics.density
            val widthPx = (if (is5x2) 250 else 250) * density // Adjust as needed
            val heightPx = (if (is5x2) 80 else 120) * density // Adjust as needed
            val bmp = ContribCalendarBuilder.buildBitmap(context, history, widthPx.toInt(), heightPx.toInt())
            views.setImageViewBitmap(R.id.contribGrid, bmp)
            
            views.setOnClickPendingIntent(R.id.widgetRoot, launchIntent(id))
            manager.updateAppWidget(id, views)
        }
    }

    private fun launchIntent(id: Int): PendingIntent =
        PendingIntent.getActivity(
            context,
            id,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}
