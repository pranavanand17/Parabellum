package com.parabellum.app.navigation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.parabellum.app.model.UserSettings
import com.parabellum.app.ui.screens.ArchiveScreen
import com.parabellum.app.ui.screens.PomodoroScreen
import com.parabellum.app.ui.screens.SettingsScreen
import com.parabellum.app.ui.screens.TasksScreen
import com.parabellum.app.viewmodel.ArchiveViewModel
import com.parabellum.app.viewmodel.PomodoroViewModel
import com.parabellum.app.viewmodel.SettingsViewModel
import com.parabellum.app.viewmodel.TasksViewModel

sealed class Screen(val route: String, val title: String, val tabLabel: String) {
    object Tasks : Screen("tasks", "Work Queue", "[01] TASKS")
    object Pomodoro : Screen("pomodoro", "Pomodoro Timer", "[02] POMODORO")
    object Archive : Screen("archive", "Work History", "ARCHIVE")
    object Settings : Screen("settings", "System Settings", "SETTINGS")
}

@Composable
fun ParabellumNavGraph(
    navController: NavHostController = rememberNavController(),
    tasksViewModel: TasksViewModel,
    archiveViewModel: ArchiveViewModel,
    pomodoroViewModel: PomodoroViewModel,
    settingsViewModel: SettingsViewModel,
    userSettings: UserSettings
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(Screen.Tasks.route, Screen.Pomodoro.route)
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                RetroBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Tasks.route) {
                TasksScreen(
                    tasksViewModel = tasksViewModel,
                    userSettings = userSettings,
                    onNavigateToArchive = { navController.navigate(Screen.Archive.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }

            composable(Screen.Pomodoro.route) {
                PomodoroScreen(
                    pomodoroViewModel = pomodoroViewModel,
                    userSettings = userSettings
                )
            }

            composable(Screen.Archive.route) {
                ArchiveScreen(
                    archiveViewModel = archiveViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun RetroBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(Screen.Tasks, Screen.Pomodoro)

            tabs.forEach { screen ->
                val isSelected = currentRoute == screen.route

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(2.dp)
                        )
                        .border(
                            if (isSelected) 1.5.dp else 1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(2.dp)
                        )
                        .clickable { onNavigate(screen.route) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isSelected) "▶ " else "  ",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = screen.tabLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
