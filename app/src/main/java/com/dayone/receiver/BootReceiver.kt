package com.dayone.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dayone.alarm.DayOneAlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var scheduler: DayOneAlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        scheduler.scheduleDailyAlarms()
    }
}
