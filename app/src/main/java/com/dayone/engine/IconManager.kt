package com.dayone.engine

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

object IconManager {
    private const val TAG = "IconManager"
    private val ALIASES = listOf(
        "com.dayone.LauncherBlue",
        "com.dayone.LauncherOrange",
        "com.dayone.LauncherRed",
        "com.dayone.LauncherGreen",
        "com.dayone.LauncherPurple"
    )

    fun updateIcon(context: Context, streak: Int) {
        val targetAlias = when {
            streak < 3 -> "com.dayone.LauncherBlue"
            streak < 7 -> "com.dayone.LauncherOrange"
            streak < 14 -> "com.dayone.LauncherRed"
            streak < 30 -> "com.dayone.LauncherPurple"
            else -> "com.dayone.LauncherGreen"
        }

        Log.d(TAG, "Updating icon for streak $streak. Target alias: $targetAlias")

        ALIASES.forEach { alias ->
            val state = if (alias == targetAlias) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            
            try {
                context.packageManager.setComponentEnabledSetting(
                    ComponentName(context, alias),
                    state,
                    PackageManager.DONT_KILL_APP
                )
                Log.d(TAG, "Alias $alias set to state $state")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to set state for alias $alias", e)
            }
        }
    }
}
