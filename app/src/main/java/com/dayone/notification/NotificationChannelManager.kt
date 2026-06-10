package com.dayone.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannelManager {
    const val CHANNEL_DAILY = "dayone_daily"
    const val CHANNEL_MILESTONE = "dayone_milestone"
    const val CHANNEL_WEEKLY = "dayone_weekly"

    fun registerChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_DAILY,
                    "Daily Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Habit daily check-in"
                },
                NotificationChannel(
                    CHANNEL_MILESTONE,
                    "Milestones",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Streak achievements"
                },
                NotificationChannel(
                    CHANNEL_WEEKLY,
                    "Weekly Summary",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Weekly progress report"
                }
            )
            
            manager.createNotificationChannels(channels)
        }
    }
}
