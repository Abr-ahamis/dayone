package com.dayone.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.dayone.MainActivity
import com.dayone.R
import com.dayone.data.StreakStateEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesStore: com.dayone.data.PreferencesStore
) {
    fun showNotification(state: StreakStateEntity, title: String, body: String, channelId: String = NotificationChannelManager.CHANNEL_DAILY) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val prefs = runBlocking { preferencesStore.preferences.first() }
        val soundUri = if (prefs.notificationSound != "default") android.net.Uri.parse(prefs.notificationSound) else null

        val bannerViews = RemoteViews(context.packageName, R.layout.notification_banner)
        bannerViews.setTextViewText(R.id.notificationTitle, title)
        bannerViews.setTextViewText(R.id.notificationBody, body)
        bannerViews.setTextViewText(R.id.notificationEmoji, state.emoji)

        val expandedViews = RemoteViews(context.packageName, R.layout.notification_expanded)
        expandedViews.setTextViewText(R.id.notificationTitleLarge, title)
        expandedViews.setTextViewText(R.id.notificationEmojiLarge, state.emoji)
        expandedViews.setTextViewText(R.id.notificationSubline, "${state.habitName} · Day ${state.currentStreak}")
        
        expandedViews.setTextViewText(R.id.statLeftValue, state.currentStreak.toString())
        expandedViews.setTextViewText(R.id.statCenterValue, state.currentStreak.toString()) 
        expandedViews.setTextViewText(R.id.statRightValue, "20") 
        
        val progress = (state.currentStreak % 20) * 5
        expandedViews.setProgressBar(R.id.notificationProgress, 100, progress, false)
        expandedViews.setTextViewText(R.id.progressValue, "Day ${state.currentStreak}")
        expandedViews.setTextViewText(R.id.messageText, body)
        expandedViews.setTextViewText(R.id.messageIcon, state.emoji)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.image_icon)
            .setCustomContentView(bannerViews)
            .setCustomBigContentView(expandedViews)
            .setCustomHeadsUpContentView(bannerViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            
        if (soundUri != null) {
            notificationBuilder.setSound(soundUri)
        }

        manager.notify(1001, notificationBuilder.build())
    }
}
