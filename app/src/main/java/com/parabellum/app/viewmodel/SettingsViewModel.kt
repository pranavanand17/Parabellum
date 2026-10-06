package com.parabellum.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parabellum.app.data.datastore.SettingsRepository
import com.parabellum.app.model.AccentColor
import com.parabellum.app.model.AnimationIntensity
import com.parabellum.app.model.ThemeOption
import com.parabellum.app.model.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val userSettings: StateFlow<UserSettings> = settingsRepository.userSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings()
        )

    fun setThemeOption(option: ThemeOption) {
        viewModelScope.launch {
            settingsRepository.updateThemeOption(option)
        }
    }

    fun setAccentColor(accent: AccentColor) {
        viewModelScope.launch {
            settingsRepository.updateAccentColor(accent)
        }
    }

    fun setAnimationIntensity(intensity: AnimationIntensity) {
        viewModelScope.launch {
            settingsRepository.updateAnimationIntensity(intensity)
        }
    }

    fun setCompactTaskLayout(compact: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateCompactTaskLayout(compact)
        }
    }

    fun setFocusDuration(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.updateFocusDuration(minutes)
        }
    }

    fun setShortBreakDuration(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.updateShortBreakDuration(minutes)
        }
    }

    fun setLongBreakDuration(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.updateLongBreakDuration(minutes)
        }
    }

    fun setSessionsBeforeLongBreak(sessions: Int) {
        viewModelScope.launch {
            settingsRepository.updateSessionsBeforeLongBreak(sessions)
        }
    }

    class Factory(private val settingsRepository: SettingsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(settingsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
