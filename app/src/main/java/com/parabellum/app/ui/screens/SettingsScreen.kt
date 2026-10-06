package com.parabellum.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parabellum.app.model.AccentColor
import com.parabellum.app.model.AnimationIntensity
import com.parabellum.app.model.ThemeOption
import com.parabellum.app.model.UserSettings
import com.parabellum.app.ui.components.ParabellumPanel
import com.parabellum.app.ui.components.RetroButton
import com.parabellum.app.ui.components.RetroHeaderBar
import com.parabellum.app.ui.components.RetroIconButton
import com.parabellum.app.ui.theme.getAccentColorValue
import com.parabellum.app.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val userSettings by settingsViewModel.userSettings.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
    ) {
        // Top Header Bar
        RetroHeaderBar(
            title = "PARABELLUM // SYSTEM SETTINGS",
            subtitle = "SYSTEM CONFIGURATION & PREFERENCES",
            statusText = "SYS-CONFIG",
            actions = {
                RetroIconButton(
                    icon = Icons.Default.ArrowBack,
                    contentDescription = "Return to Queue",
                    onClick = onNavigateBack
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Visual Theme Selection
            SettingsSectionPanel(title = "01 // VISUAL THEME PREFERENCE") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeOption.values().forEach { theme ->
                        val isSelected = userSettings.themeOption == theme
                        SelectableOptionRow(
                            label = theme.label,
                            isSelected = isSelected,
                            onSelect = { settingsViewModel.setThemeOption(theme) }
                        )
                    }
                }
            }

            // 2. Accent Color Selection
            SettingsSectionPanel(title = "02 // ACCENT COLOR PALETTE") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AccentColor.values().forEach { accent ->
                        val isSelected = userSettings.accentColor == accent
                        val colorVal = getAccentColorValue(accent)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { settingsViewModel.setAccentColor(accent) }
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(colorVal, RoundedCornerShape(2.dp))
                                    .border(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(2.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Text(
                                        text = "✓",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = accent.name,
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // 3. UI Motion & Layout Options
            SettingsSectionPanel(title = "03 // UI MOTION & COMPACTNESS") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "ANIMATION INTENSITY:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnimationIntensity.values().forEach { intensity ->
                            val isSelected = userSettings.animationIntensity == intensity
                            RetroButton(
                                text = intensity.label,
                                onClick = { settingsViewModel.setAnimationIntensity(intensity) },
                                isPrimary = isSelected,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "TASK DENSITY LAYOUT:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RetroButton(
                            text = "STANDARD DENSITY",
                            onClick = { settingsViewModel.setCompactTaskLayout(false) },
                            isPrimary = !userSettings.compactTaskLayout,
                            modifier = Modifier.weight(1f)
                        )
                        RetroButton(
                            text = "COMPACT DENSITY",
                            onClick = { settingsViewModel.setCompactTaskLayout(true) },
                            isPrimary = userSettings.compactTaskLayout,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 4. Pomodoro Timer Customization
            SettingsSectionPanel(title = "04 // POMODORO TIMER CONFIGURATION") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberSettingRow(
                        label = "FOCUS DURATION (MIN):",
                        value = userSettings.focusDurationMinutes,
                        onDecrement = { settingsViewModel.setFocusDuration(userSettings.focusDurationMinutes - 5) },
                        onIncrement = { settingsViewModel.setFocusDuration(userSettings.focusDurationMinutes + 5) }
                    )

                    NumberSettingRow(
                        label = "SHORT BREAK (MIN):",
                        value = userSettings.shortBreakDurationMinutes,
                        onDecrement = { settingsViewModel.setShortBreakDuration(userSettings.shortBreakDurationMinutes - 1) },
                        onIncrement = { settingsViewModel.setShortBreakDuration(userSettings.shortBreakDurationMinutes + 1) }
                    )

                    NumberSettingRow(
                        label = "LONG BREAK (MIN):",
                        value = userSettings.longBreakDurationMinutes,
                        onDecrement = { settingsViewModel.setLongBreakDuration(userSettings.longBreakDurationMinutes - 5) },
                        onIncrement = { settingsViewModel.setLongBreakDuration(userSettings.longBreakDurationMinutes + 5) }
                    )

                    NumberSettingRow(
                        label = "SESSIONS BEFORE LONG BREAK:",
                        value = userSettings.focusSessionsBeforeLongBreak,
                        onDecrement = { settingsViewModel.setSessionsBeforeLongBreak(userSettings.focusSessionsBeforeLongBreak - 1) },
                        onIncrement = { settingsViewModel.setSessionsBeforeLongBreak(userSettings.focusSessionsBeforeLongBreak + 1) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSectionPanel(
    title: String,
    content: @Composable () -> Unit
) {
    ParabellumPanel(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            content()
        }
    }
}

@Composable
private fun SelectableOptionRow(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(2.dp)
            )
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(2.dp)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = if (isSelected) "[ ACTIVE ]" else "[ SELECT ]",
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun NumberSettingRow(
    label: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            RetroButton(
                text = "-",
                onClick = onDecrement
            )
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(2.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp))
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$value",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            RetroButton(
                text = "+",
                onClick = onIncrement
            )
        }
    }
}
