package com.parabellum.app.model

enum class PomodoroMode(val title: String, val subtitle: String) {
    FOCUS("FOCUS CYCLE", "MACRODATA REFINEMENT IN PROGRESS"),
    SHORT_BREAK("SHORT REST CYCLE", "MANDATORY REST PERIOD // 05 MIN"),
    LONG_BREAK("LONG REST CYCLE", "EXTENDED WELLNESS BREAK // 15 MIN")
}

data class PomodoroState(
    val mode: PomodoroMode = PomodoroMode.FOCUS,
    val remainingSeconds: Int = 25 * 60,
    val totalSeconds: Int = 25 * 60,
    val isRunning: Boolean = false,
    val currentSessionCount: Int = 1,
    val totalSessionsBeforeLongBreak: Int = 4
) {
    val formattedTime: String
        get() {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    val progressFraction: Float
        get() = if (totalSeconds > 0) 1f - (remainingSeconds.toFloat() / totalSeconds.toFloat()) else 0f
}
