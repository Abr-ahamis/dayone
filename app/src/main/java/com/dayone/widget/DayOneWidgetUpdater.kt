package com.dayone.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import com.dayone.MainActivity
import com.dayone.R
import com.dayone.data.DayOneRepository
import com.dayone.domain.StreakColorEngine
import com.dayone.domain.formatShort
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DayOneWidgetUpdater @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DayOneRepository
) {
    suspend fun updateAllWidgets() {
        val manager = AppWidgetManager.getInstance(context)
        update(manager, ComponentName(context, CompactDayOneWidgetProvider::class.java), R.layout.widget_compact)
        update(manager, ComponentName(context, ExpandedDayOneWidgetProvider::class.java), R.layout.widget_expanded)
    }

    suspend fun update(manager: AppWidgetManager, component: ComponentName, layoutId: Int) {
        val state = repository.currentState()
        val ids = manager.getAppWidgetIds(component)
        val color = StreakColorEngine.getStreakColor(state.currentStreak, state.streakBreaks)
        val dark = StreakColorEngine.darken(color, 0.35f)
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, layoutId)
            views.setInt(R.id.widgetRoot, "setBackgroundColor", color)
            views.setInt(R.id.widgetAccent, "setBackgroundColor", dark)
            views.setTextViewText(R.id.widgetStreak, state.currentStreak.toString())
            views.setTextViewText(R.id.widgetLabel, if (layoutId == R.layout.widget_compact) "Days" else "Days Strong")
            views.setTextViewText(R.id.widgetIcon, StreakColorEngine.icon(state.currentStreak))
            if (layoutId == R.layout.widget_expanded) {
                views.setTextViewText(R.id.widgetDate, formatShort(System.currentTimeMillis() / 86_400_000L))
                views.setTextViewText(R.id.widgetStatus, if (state.currentStreak >= 7) "On Fire" else "On Track")
                views.setTextViewText(R.id.widgetHabit, state.habitName)
            }
            views.setOnClickPendingIntent(R.id.widgetRoot, launchIntent())
            manager.updateAppWidget(id, views)
        }
    }

    private fun launchIntent(): PendingIntent =
        PendingIntent.getActivity(
            context,
            55,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}
