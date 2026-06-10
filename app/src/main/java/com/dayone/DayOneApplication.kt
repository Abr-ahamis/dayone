package com.dayone

import android.app.Application
import com.dayone.alarm.DayOneAlarmScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DayOneApplication : Application() {
    @Inject lateinit var alarmScheduler: DayOneAlarmScheduler

    override fun onCreate() {
        super.onCreate()
        com.dayone.notification.NotificationChannelManager.registerChannels(this)
        alarmScheduler.scheduleDailyAlarms()
    }
}
