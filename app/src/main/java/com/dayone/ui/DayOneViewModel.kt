package com.dayone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayone.data.CalendarEntryEntity
import com.dayone.data.DayOneRepository
import com.dayone.data.PreferencesStore
import com.dayone.data.StreakStateEntity
import com.dayone.data.UserPreferences
import com.dayone.domain.todayEpochDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DayOneUiState(
    val loading: Boolean = true,
    val state: StreakStateEntity? = null,
    val history: List<CalendarEntryEntity> = emptyList(),
    val verse: String = "",
    val preferences: UserPreferences = UserPreferences()
)

@HiltViewModel
class DayOneViewModel @Inject constructor(
    private val repository: DayOneRepository,
    private val preferencesStore: PreferencesStore
) : ViewModel() {
    private val today = todayEpochDay()
    private val rangeStart = today - 370
    private val rangeEnd = today + 31

    val uiState: StateFlow<DayOneUiState> = combine(
        repository.observeSnapshot(rangeStart, rangeEnd),
        preferencesStore.preferences
    ) { snapshot, preferences ->
        DayOneUiState(
            loading = false,
            state = snapshot.state,
            history = snapshot.history,
            verse = snapshot.verse,
            preferences = preferences
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DayOneUiState())

    fun completeToday() = viewModelScope.launch { repository.completeToday() }
    fun resetStreak() = viewModelScope.launch { repository.resetStreak() }
    fun clearAll() = viewModelScope.launch { repository.clearAll() }
    fun setDarkTheme(enabled: Boolean) = viewModelScope.launch { preferencesStore.setDarkTheme(enabled) }
    fun setNotifications(enabled: Boolean) = viewModelScope.launch { preferencesStore.setNotifications(enabled) }
}
