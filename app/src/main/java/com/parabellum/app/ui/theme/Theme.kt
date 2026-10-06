package com.parabellum.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.parabellum.app.model.AccentColor
import com.parabellum.app.model.ThemeOption

fun getAccentColorValue(accentColor: AccentColor): Color {
    return when (accentColor) {
        AccentColor.GREEN -> LumonGreen
        AccentColor.TEAL -> MutedTeal
        AccentColor.BLUE -> CorporateBlue
        AccentColor.AMBER -> TerminalAmber
        AccentColor.RED -> AlertRed
    }
}

fun createParabellumColorScheme(
    themeOption: ThemeOption,
    accentColor: AccentColor
): ColorScheme {
    val accent = getAccentColorValue(accentColor)

    return when (themeOption) {
        ThemeOption.PARABELLUM -> lightColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = CreamSurfaceVariant,
            onPrimaryContainer = DarkCharcoal,
            secondary = DarkCharcoal,
            onSecondary = CreamBackground,
            background = CreamBackground,
            onBackground = DarkCharcoal,
            surface = CreamSurface,
            onSurface = DarkCharcoal,
            surfaceVariant = CreamSurfaceVariant,
            onSurfaceVariant = DarkCharcoal,
            outline = CreamBorder,
            outlineVariant = Color(0xFFA8A294)
        )
        ThemeOption.LIGHT -> lightColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE5E5E5),
            onPrimaryContainer = Color.Black,
            secondary = Color.Black,
            onSecondary = Color.White,
            background = LightBg,
            onBackground = Color(0xFF111111),
            surface = LightSurface,
            onSurface = Color(0xFF111111),
            surfaceVariant = Color(0xFFE0E0E0),
            onSurfaceVariant = Color(0xFF111111),
            outline = Color(0xFFCCCCCC),
            outlineVariant = Color(0xFF999999)
        )
        ThemeOption.DARK -> darkColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = DarkSurfaceVariant,
            onPrimaryContainer = DarkText,
            secondary = DarkText,
            onSecondary = DarkBg,
            background = DarkBg,
            onBackground = DarkText,
            surface = DarkSurface,
            onSurface = DarkText,
            surfaceVariant = DarkSurfaceVariant,
            onSurfaceVariant = DarkText,
            outline = DarkBorder,
            outlineVariant = Color(0xFF484E54)
        )
    }
}

@Composable
fun ParabellumTheme(
    themeOption: ThemeOption = ThemeOption.PARABELLUM,
    accentColor: AccentColor = AccentColor.GREEN,
    content: @Composable () -> Unit
) {
    val colorScheme = createParabellumColorScheme(themeOption, accentColor)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ParabellumTypography,
        shapes = ParabellumShapes,
        content = content
    )
}
