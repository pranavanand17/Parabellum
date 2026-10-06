package com.parabellum.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parabellum.app.data.repository.TaskRepository
import com.parabellum.app.model.TaskEntity
import com.parabellum.app.widget.ParabellumWidget
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArchiveViewModel(
    private val repository: TaskRepository,
    private val appContext: Context
) : ViewModel() {

    private val dateFormatter = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)

    val completedTasksGroupedByDate: StateFlow<Map<String, List<TaskEntity>>> = repository.completedTasks
        .map { tasks ->
            tasks.groupBy { task ->
                val date = Date(task.completedAt ?: task.createdAt)
                dateFormatter.format(date).uppercase(Locale.US)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun restoreTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.restoreTask(task)
            updateWidget()
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
            updateWidget()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearCompletedHistory()
            updateWidget()
        }
    }

    private fun updateWidget() {
        viewModelScope.launch {
            try {
                ParabellumWidget.updateAll(appContext)
            } catch (e: Exception) {
                // Ignore widget update errors
            }
        }
    }

    class Factory(
        private val repository: TaskRepository,
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArchiveViewModel::class.java)) {
                return ArchiveViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
