package com.parabellum.app

import android.app.Application
import com.parabellum.app.data.database.ParabellumDatabase
import com.parabellum.app.data.datastore.SettingsRepository
import com.parabellum.app.data.repository.TaskRepository

class ParabellumApp : Application() {

    val database by lazy { ParabellumDatabase.getDatabase(this) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }
    val settingsRepository by lazy { SettingsRepository(this) }

    override fun onCreate() {
        super.onCreate()
    }
}
