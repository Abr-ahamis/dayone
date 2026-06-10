package com.dayone.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dayone.alarm.DayOneAlarmScheduler
import com.dayone.data.DayOneRepository
import com.dayone.widget.DayOneWidgetUpdater
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DayOneAlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var repository: DayOneRepository
    @Inject lateinit var scheduler: DayOneAlarmScheduler
    @Inject lateinit var widgetUpdater: DayOneWidgetUpdater

    override fun onReceive(context: Context, intent: Intent) {
        android.util.Log.d("DayOneReceiver", "Received intent: ${intent.action}")
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                if (intent.action == "com.dayone.SYNC_WIDGET") {
                    val json = intent.getStringExtra("data")
                    if (json != null) {
                        repository.syncFromWebView(json)
                        widgetUpdater.updateAllWidgets()
                    }
                } else {
                    when (DayOneAlarmScheduler.Action.from(intent.action)) {
                        DayOneAlarmScheduler.Action.Morning -> {
                            // repository.runMorningCycle() is removed, handle as needed or ignore
                            scheduler.schedule(DayOneAlarmScheduler.Action.Morning, 6, 30)
                        }
                        DayOneAlarmScheduler.Action.Noon -> {
                            widgetUpdater.updateAllWidgets()
                            scheduler.schedule(DayOneAlarmScheduler.Action.Noon, 12, 0)
                        }
                        null -> Unit
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
