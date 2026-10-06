package com.parabellum.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parabellum.app.navigation.ParabellumNavGraph
import com.parabellum.app.ui.theme.ParabellumTheme
import com.parabellum.app.viewmodel.ArchiveViewModel
import com.parabellum.app.viewmodel.PomodoroViewModel
import com.parabellum.app.viewmodel.SettingsViewModel
import com.parabellum.app.viewmodel.TasksViewModel

class MainActivity : ComponentActivity() {

    private val app by lazy { application as ParabellumApp }

    private val tasksViewModel: TasksViewModel by viewModels {
        TasksViewModel.Factory(app.taskRepository, applicationContext)
    }

    private val archiveViewModel: ArchiveViewModel by viewModels {
        ArchiveViewModel.Factory(app.taskRepository, applicationContext)
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModel.Factory(app.settingsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val userSettings by settingsViewModel.userSettings.collectAsState()

            val pomodoroViewModel: PomodoroViewModel = viewModel(
                factory = PomodoroViewModel.Factory(userSettings)
            )

            ParabellumTheme(
                themeOption = userSettings.themeOption,
                accentColor = userSettings.accentColor
            ) {
                ParabellumNavGraph(
                    tasksViewModel = tasksViewModel,
                    archiveViewModel = archiveViewModel,
                    pomodoroViewModel = pomodoroViewModel,
                    settingsViewModel = settingsViewModel,
                    userSettings = userSettings
                )
            }
        }
    }
}
