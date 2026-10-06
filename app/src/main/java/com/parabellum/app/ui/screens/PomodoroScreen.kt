package com.parabellum.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parabellum.app.model.PomodoroMode
import com.parabellum.app.model.UserSettings
import com.parabellum.app.ui.components.ParabellumPanel
import com.parabellum.app.ui.components.RetroButton
import com.parabellum.app.ui.components.RetroHeaderBar
import com.parabellum.app.viewmodel.PomodoroViewModel

@Composable
fun PomodoroScreen(
    pomodoroViewModel: PomodoroViewModel,
    userSettings: UserSettings
) {
    val state by pomodoroViewModel.pomodoroState.collectAsState()

    // Sync user settings with viewModel when settings change
    LaunchedEffect(userSettings) {
        pomodoroViewModel.updateSettings(userSettings)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header Bar
        RetroHeaderBar(
            title = "PARABELLUM // POMODORO TIMER",
            subtitle = "MACRODATA TEMPORAL FOCUS CONTROLLER",
            statusText = if (state.isRunning) "TIMER // RUNNING" else "TIMER // PAUSED"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Main Timer Card Panel
        ParabellumPanel(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (state.isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            borderWidth = if (state.isRunning) 2.dp else 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mode Tag Header
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(2.dp)
                        )
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "[ ${state.mode.title} ]",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = state.mode.subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                // Large Countdown Display
                Text(
                    text = state.formattedTime,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    color = if (state.isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                // Session Count Indicators
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SESSION:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    for (i in 1..state.totalSessionsBeforeLongBreak) {
                        val isCompleted = i < state.currentSessionCount || (i == state.currentSessionCount && state.mode != PomodoroMode.FOCUS)
                        val isCurrent = i == state.currentSessionCount && state.mode == PomodoroMode.FOCUS

                        Box(
                            modifier = Modifier
                                .size(width = 24.dp, height = 12.dp)
                                .background(
                                    when {
                                        isCurrent -> MaterialTheme.colorScheme.primary
                                        isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    RoundedCornerShape(1.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(1.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$i",
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 8.sp,
                                color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Progress Bar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val percent = (state.progressFraction * 100).toInt()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(1.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(1.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(state.progressFraction)
                                .height(10.dp)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "CYCLE PROGRESS: $percent%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                // Control Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RetroButton(
                        text = if (state.isRunning) "PAUSE" else "START",
                        onClick = { pomodoroViewModel.toggleStartPause() },
                        isPrimary = true,
                        icon = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        modifier = Modifier.width(110.dp)
                    )

                    RetroButton(
                        text = "RESET",
                        onClick = { pomodoroViewModel.resetTimer() },
                        icon = Icons.Default.Refresh
                    )

                    RetroButton(
                        text = "SKIP",
                        onClick = { pomodoroViewModel.skipCurrentCycle() },
                        icon = Icons.Default.SkipNext
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preset Overview Box
        ParabellumPanel(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FOCUS: ${userSettings.focusDurationMinutes}M",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "|",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "SHORT: ${userSettings.shortBreakDurationMinutes}M",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "|",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "LONG: ${userSettings.longBreakDurationMinutes}M",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
