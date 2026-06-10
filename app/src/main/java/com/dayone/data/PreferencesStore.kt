package com.dayone.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dayOneDataStore by preferencesDataStore("dayone_preferences")

data class UserPreferences(
    val darkTheme: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val notificationTime: String = "09:00",
    val notificationSound: String = "default",
    val widgetColor: String = "#4F8EF7",
    val widgetGloss: Boolean = true,
    val dynamicIcon: Boolean = true
)

@Singleton
class PreferencesStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val Theme = stringPreferencesKey("theme")
        val Notifications = booleanPreferencesKey("notifications")
        val NotifTime = stringPreferencesKey("notif_time")
        val NotifSound = stringPreferencesKey("notif_sound")
        val WidgetColor = stringPreferencesKey("widget_color")
        val WidgetGloss = booleanPreferencesKey("widget_gloss")
        val DynamicIcon = booleanPreferencesKey("dynamic_icon")
    }

    val preferences: Flow<UserPreferences> = context.dayOneDataStore.data.map { prefs ->
        UserPreferences(
            darkTheme = prefs[Keys.Theme] != "light",
            notificationsEnabled = prefs[Keys.Notifications] ?: true,
            notificationTime = prefs[Keys.NotifTime] ?: "09:00",
            notificationSound = prefs[Keys.NotifSound] ?: "default",
            widgetColor = prefs[Keys.WidgetColor] ?: "#4F8EF7",
            widgetGloss = prefs[Keys.WidgetGloss] ?: true,
            dynamicIcon = prefs[Keys.DynamicIcon] ?: true
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dayOneDataStore.edit { it[Keys.Theme] = if (enabled) "dark" else "light" }
    }

    suspend fun setNotifications(enabled: Boolean) {
        context.dayOneDataStore.edit { it[Keys.Notifications] = enabled }
    }

    suspend fun setNotificationTime(time: String) {
        context.dayOneDataStore.edit { it[Keys.NotifTime] = time }
    }

    suspend fun setNotificationSound(sound: String) {
        context.dayOneDataStore.edit { it[Keys.NotifSound] = sound }
    }

    suspend fun setWidgetColor(color: String) {
        context.dayOneDataStore.edit { it[Keys.WidgetColor] = color }
    }

    suspend fun setWidgetGloss(enabled: Boolean) {
        context.dayOneDataStore.edit { it[Keys.WidgetGloss] = enabled }
    }

    suspend fun setDynamicIcon(enabled: Boolean) {
        context.dayOneDataStore.edit { it[Keys.DynamicIcon] = enabled }
    }
}
