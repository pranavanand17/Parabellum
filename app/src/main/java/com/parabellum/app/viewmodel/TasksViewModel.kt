package com.parabellum.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parabellum.app.data.repository.TaskRepository
import com.parabellum.app.model.TaskEntity
import com.parabellum.app.model.TaskSection
import com.parabellum.app.widget.ParabellumWidget
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: TaskRepository,
    private val appContext: Context
) : ViewModel() {

    val pendingTasksFlow: StateFlow<List<TaskEntity>> = repository.pendingTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val tasksBySection: StateFlow<Map<TaskSection, List<TaskEntity>>> = pendingTasksFlow
        .map { tasks ->
            TaskSection.values().associateWith { section ->
                tasks.filter { it.section == section }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskSection.values().associateWith { emptyList() }
        )

    val pendingCount: StateFlow<Int> = repository.pendingCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private val _activeDepositingTaskTitle = MutableStateFlow<String?>(null)
    val activeDepositingTaskTitle: StateFlow<String?> = _activeDepositingTaskTitle.asStateFlow()

    private val _completingTaskId = MutableStateFlow<Long?>(null)
    val completingTaskId: StateFlow<Long?> = _completingTaskId.asStateFlow()

    fun addTask(title: String, section: TaskSection) {
        viewModelScope.launch {
            repository.addTask(title, section)
            updateWidget()
        }
    }

    fun completeTaskWithAnimation(task: TaskEntity, animationEnabled: Boolean = true) {
        viewModelScope.launch {
            if (animationEnabled) {
                _completingTaskId.value = task.id
                _activeDepositingTaskTitle.value = task.title
                delay(350)
            }
            repository.completeTask(task)
            _completingTaskId.value = null
            _activeDepositingTaskTitle.value = null
            updateWidget()
        }
    }

    private fun updateWidget() {
        viewModelScope.launch {
            try {
                ParabellumWidget.updateAll(appContext)
            } catch (e: Exception) {
                // Ignore widget update errors if Glance receiver isn't fully bound yet
            }
        }
    }

    class Factory(
        private val repository: TaskRepository,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
                return TasksViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
