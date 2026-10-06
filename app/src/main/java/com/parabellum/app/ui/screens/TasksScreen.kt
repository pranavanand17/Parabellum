package com.parabellum.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parabellum.app.model.AnimationIntensity
import com.parabellum.app.model.TaskEntity
import com.parabellum.app.model.TaskSection
import com.parabellum.app.model.UserSettings
import com.parabellum.app.ui.components.DepositContainerPanel
import com.parabellum.app.ui.components.ParabellumPanel
import com.parabellum.app.ui.components.RetroAddTaskDialog
import com.parabellum.app.ui.components.RetroButton
import com.parabellum.app.ui.components.RetroCheckbox
import com.parabellum.app.ui.components.RetroHeaderBar
import com.parabellum.app.ui.components.RetroIconButton
import com.parabellum.app.viewmodel.TasksViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TasksScreen(
    tasksViewModel: TasksViewModel,
    userSettings: UserSettings,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val tasksBySection by tasksViewModel.tasksBySection.collectAsState()
    val totalPendingCount by tasksViewModel.pendingCount.collectAsState()
    val activeDepositingTitle by tasksViewModel.activeDepositingTaskTitle.collectAsState()
    val completingTaskId by tasksViewModel.completingTaskId.collectAsState()

    var sectionToAddFor by remember { mutableStateOf<TaskSection?>(null) }

    val animate = userSettings.animationIntensity != AnimationIntensity.MINIMAL

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp)
        ) {
            // Top Header Bar
            RetroHeaderBar(
                title = "PARABELLUM // WORK QUEUE",
                subtitle = "TOTAL OPEN TASKS: $totalPendingCount",
                statusText = "MDR-SYS // ACTIVE",
                actions = {
                    RetroIconButton(
                        icon = Icons.Default.Archive,
                        contentDescription = "Work History Archive",
                        onClick = onNavigateToArchive
                    )
                    RetroIconButton(
                        icon = Icons.Default.Settings,
                        contentDescription = "System Settings",
                        onClick = onNavigateToSettings
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 5 Vertical Sections List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TaskSection.values().forEach { section ->
                    val sectionTasks = tasksBySection[section] ?: emptyList()

                    item(key = "section_header_${section.name}") {
                        SectionHeaderPanel(
                            section = section,
                            taskCount = sectionTasks.size,
                            onAddTaskClick = { sectionToAddFor = section }
                        )
                    }

                    if (sectionTasks.isEmpty()) {
                        item(key = "empty_${section.name}") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, bottom = 4.dp)
                            ) {
                                Text(
                                    text = "// QUEUE EMPTY",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                                )
                            }
                        }
                    } else {
                        items(
                            items = sectionTasks,
                            key = { it.id }
                        ) { task ->
                            val isCompleting = completingTaskId == task.id
                            TaskRowItem(
                                task = task,
                                compact = userSettings.compactTaskLayout,
                                isCompleting = isCompleting,
                                onCheck = {
                                    tasksViewModel.completeTaskWithAnimation(task, animationEnabled = animate)
                                }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Bottom Archive Deposit Animation Panel
        DepositContainerPanel(
            activeDepositingTaskTitle = activeDepositingTitle,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Add Task Dialog
    sectionToAddFor?.let { section ->
        RetroAddTaskDialog(
            sectionTitle = section.displayLabel,
            onDismiss = { sectionToAddFor = null },
            onConfirm = { title ->
                tasksViewModel.addTask(title, section)
                sectionToAddFor = null
            }
        )
    }
}

@Composable
private fun SectionHeaderPanel(
    section: TaskSection,
    taskCount: Int,
    onAddTaskClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = section.displayLabel,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(2.dp)
                        )
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "[ $taskCount ]",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            RetroButton(
                text = "+ ADD",
                onClick = onAddTaskClick,
                icon = Icons.Default.Add
            )
        }

        Text(
            text = section.description,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp
        )
    }
}

@Composable
private fun TaskRowItem(
    task: TaskEntity,
    compact: Boolean,
    isCompleting: Boolean,
    onCheck: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.US) }
    val timeString = remember(task.createdAt) { timeFormat.format(Date(task.createdAt)) }

    val alpha by animateFloatAsState(
        targetValue = if (isCompleting) 0.3f else 1.0f,
        animationSpec = tween(300)
    )

    val bgColor = if (isCompleting) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    ParabellumPanel(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(vertical = 2.dp),
        backgroundColor = bgColor,
        borderColor = if (isCompleting) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = if (compact) 6.dp else 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                RetroCheckbox(
                    checked = task.isCompleted || isCompleting,
                    onCheckedChange = { onCheck() }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = if (compact) 1 else 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = timeString,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
    }
}
