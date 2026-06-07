package com.dayone.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompactDayOneWidgetProvider : AppWidgetProvider() {
    @Inject lateinit var updater: DayOneWidgetUpdater

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch { updater.updateAllWidgets() }
    }
}
