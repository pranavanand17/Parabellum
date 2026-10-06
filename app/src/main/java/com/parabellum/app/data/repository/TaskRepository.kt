package com.parabellum.app.data.repository

import com.parabellum.app.data.database.TaskDao
import com.parabellum.app.model.TaskEntity
import com.parabellum.app.model.TaskSection
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val pendingTasks: Flow<List<TaskEntity>> = taskDao.getPendingTasks()
    val completedTasks: Flow<List<TaskEntity>> = taskDao.getCompletedTasks()
    val pendingCount: Flow<Int> = taskDao.getPendingTaskCountFlow()

    suspend fun addTask(title: String, section: TaskSection): Long {
        val newTask = TaskEntity(
            title = title.trim(),
            section = section,
            isCompleted = false,
            createdAt = System.currentTimeMillis()
        )
        return taskDao.insertTask(newTask)
    }

    suspend fun completeTask(task: TaskEntity) {
        val updated = task.copy(
            isCompleted = true,
            completedAt = System.currentTimeMillis()
        )
        taskDao.updateTask(updated)
    }

    suspend fun restoreTask(task: TaskEntity) {
        val updated = task.copy(
            isCompleted = false,
            completedAt = null
        )
        taskDao.updateTask(updated)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun clearCompletedHistory() {
        taskDao.clearCompletedHistory()
    }

    suspend fun getWidgetData(): Pair<Int, Int> {
        val totalPending = taskDao.getPendingTaskCountDirect()
        val immediatePending = taskDao.getImmediateTaskCountDirect()
        return Pair(totalPending, immediatePending)
    }
}
