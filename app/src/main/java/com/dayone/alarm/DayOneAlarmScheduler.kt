package com.dayone.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dayone.receiver.DayOneAlarmReceiver
import java.time.LocalDateTime
import java.time.ZoneId

class DayOneAlarmScheduler(private val context: Context) {
    fun scheduleDailyAlarms() {
        schedule(Action.Morning, 6, 30)
        schedule(Action.Noon, 12, 0)
    }

    fun schedule(action: Action, hour: Int, minute: Int) {
        val manager = context.getSystemService(AlarmManager::class.java)
        val intent = Intent(context, DayOneAlarmReceiver::class.java).setAction(action.intentAction)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            action.requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = LocalDateTime.now()
        var trigger = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!trigger.isAfter(now)) trigger = trigger.plusDays(1)
        val millis = trigger.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !manager.canScheduleExactAlarms()) {
            manager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        } else {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
        }
    }

    enum class Action(val intentAction: String, val requestCode: Int) {
        Morning("com.dayone.action.MORNING_0630", 630),
        Noon("com.dayone.action.NOON_1200", 1200);

        companion object {
            fun from(value: String?): Action? = entries.firstOrNull { it.intentAction == value }
        }
    }
}
