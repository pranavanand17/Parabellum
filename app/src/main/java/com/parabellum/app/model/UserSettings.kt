package com.parabellum.app.model

enum class ThemeOption(val label: String) {
    PARABELLUM("PARABELLUM RETRO"),
    LIGHT("CLINICAL LIGHT"),
    DARK("TERMINAL DARK")
}

enum class AccentColor(val label: String, val hexCode: String) {
    GREEN("LUMON GREEN", "#1B4D3E"),
    TEAL("MUTED TEAL", "#2E5A52"),
    BLUE("CORPORATE BLUE", "#2C4D6F"),
    AMBER("TERMINAL AMBER", "#8C6212"),
    RED("ALERT RED", "#7A2525")
}

enum class AnimationIntensity(val label: String) {
    NORMAL("NORMAL"),
    MINIMAL("MINIMAL")
}

data class UserSettings(
    val themeOption: ThemeOption = ThemeOption.PARABELLUM,
    val accentColor: AccentColor = AccentColor.GREEN,
    val animationIntensity: AnimationIntensity = AnimationIntensity.NORMAL,
    val compactTaskLayout: Boolean = false,
    val focusDurationMinutes: Int = 25,
    val shortBreakDurationMinutes: Int = 5,
    val longBreakDurationMinutes: Int = 15,
    val focusSessionsBeforeLongBreak: Int = 4
)
