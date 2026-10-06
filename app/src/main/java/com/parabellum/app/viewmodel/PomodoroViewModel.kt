package com.parabellum.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parabellum.app.model.PomodoroMode
import com.parabellum.app.model.PomodoroState
import com.parabellum.app.model.UserSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PomodoroViewModel(
    private var userSettings: UserSettings = UserSettings()
) : ViewModel() {

    private val _pomodoroState = MutableStateFlow(
        PomodoroState(
            mode = PomodoroMode.FOCUS,
            remainingSeconds = userSettings.focusDurationMinutes * 60,
            totalSeconds = userSettings.focusDurationMinutes * 60,
            isRunning = false,
            currentSessionCount = 1,
            totalSessionsBeforeLongBreak = userSettings.focusSessionsBeforeLongBreak
        )
    )
    val pomodoroState: StateFlow<PomodoroState> = _pomodoroState.asStateFlow()

    private var timerJob: Job? = null

    fun updateSettings(settings: UserSettings) {
        this.userSettings = settings
        if (!_pomodoroState.value.isRunning) {
            resetTimerToCurrentMode()
        }
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return
        _pomodoroState.update { it.copy(isRunning = true) }

        timerJob = viewModelScope.launch {
            while (_pomodoroState.value.remainingSeconds > 0 && _pomodoroState.value.isRunning) {
                delay(1000L)
                _pomodoroState.update { current ->
                    if (current.remainingSeconds > 1) {
                        current.copy(remainingSeconds = current.remainingSeconds - 1)
                    } else {
                        current.copy(remainingSeconds = 0)
                    }
                }
            }

            if (_pomodoroState.value.remainingSeconds == 0) {
                onCycleComplete()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
        _pomodoroState.update { it.copy(isRunning = false) }
    }

    fun toggleStartPause() {
        if (_pomodoroState.value.isRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    fun resetTimer() {
        pauseTimer()
        resetTimerToCurrentMode()
    }

    fun skipCurrentCycle() {
        pauseTimer()
        onCycleComplete()
    }

    private fun onCycleComplete() {
        _pomodoroState.update { current ->
            when (current.mode) {
                PomodoroMode.FOCUS -> {
                    if (current.currentSessionCount >= userSettings.focusSessionsBeforeLongBreak) {
                        // Long Break
                        PomodoroState(
                            mode = PomodoroMode.LONG_BREAK,
                            remainingSeconds = userSettings.longBreakDurationMinutes * 60,
                            totalSeconds = userSettings.longBreakDurationMinutes * 60,
                            isRunning = false,
                            currentSessionCount = 1,
                            totalSessionsBeforeLongBreak = userSettings.focusSessionsBeforeLongBreak
                        )
                    } else {
                        // Short Break
                        PomodoroState(
                            mode = PomodoroMode.SHORT_BREAK,
                            remainingSeconds = userSettings.shortBreakDurationMinutes * 60,
                            totalSeconds = userSettings.shortBreakDurationMinutes * 60,
                            isRunning = false,
                            currentSessionCount = current.currentSessionCount,
                            totalSessionsBeforeLongBreak = userSettings.focusSessionsBeforeLongBreak
                        )
                    }
                }
                PomodoroMode.SHORT_BREAK -> {
                    // Back to Focus for next session
                    val nextSession = current.currentSessionCount + 1
                    PomodoroState(
                        mode = PomodoroMode.FOCUS,
                        remainingSeconds = userSettings.focusDurationMinutes * 60,
                        totalSeconds = userSettings.focusDurationMinutes * 60,
                        isRunning = false,
                        currentSessionCount = nextSession,
                        totalSessionsBeforeLongBreak = userSettings.focusSessionsBeforeLongBreak
                    )
                }
                PomodoroMode.LONG_BREAK -> {
                    // Restart session count to 1
                    PomodoroState(
                        mode = PomodoroMode.FOCUS,
                        remainingSeconds = userSettings.focusDurationMinutes * 60,
                        totalSeconds = userSettings.focusDurationMinutes * 60,
                        isRunning = false,
                        currentSessionCount = 1,
                        totalSessionsBeforeLongBreak = userSettings.focusSessionsBeforeLongBreak
                    )
                }
            }
        }
    }

    private fun resetTimerToCurrentMode() {
        val current = _pomodoroState.value
        val duration = when (current.mode) {
            PomodoroMode.FOCUS -> userSettings.focusDurationMinutes * 60
            PomodoroMode.SHORT_BREAK -> userSettings.shortBreakDurationMinutes * 60
            PomodoroMode.LONG_BREAK -> userSettings.longBreakDurationMinutes * 60
        }
        _pomodoroState.value = current.copy(
            remainingSeconds = duration,
            totalSeconds = duration,
            isRunning = false,
            totalSessionsBeforeLongBreak = userSettings.focusSessionsBeforeLongBreak
        )
    }

    class Factory(private val initialSettings: UserSettings) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
                return PomodoroViewModel(initialSettings) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
