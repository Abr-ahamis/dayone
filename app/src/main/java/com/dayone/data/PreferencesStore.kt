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
    val notificationsEnabled: Boolean = true
)

@Singleton
class PreferencesStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val Theme = stringPreferencesKey("theme")
        val Notifications = booleanPreferencesKey("notifications")
    }

    val preferences: Flow<UserPreferences> = context.dayOneDataStore.data.map { prefs ->
        UserPreferences(
            darkTheme = prefs[Keys.Theme] != "light",
            notificationsEnabled = prefs[Keys.Notifications] ?: true
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dayOneDataStore.edit { it[Keys.Theme] = if (enabled) "dark" else "light" }
    }

    suspend fun setNotifications(enabled: Boolean) {
        context.dayOneDataStore.edit { it[Keys.Notifications] = enabled }
    }
}
