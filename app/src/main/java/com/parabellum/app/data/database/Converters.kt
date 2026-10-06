package com.parabellum.app.data.database

import androidx.room.TypeConverter
import com.parabellum.app.model.TaskSection

class Converters {
    @TypeConverter
    fun fromTaskSection(section: TaskSection): String = section.name

    @TypeConverter
    fun toTaskSection(value: String): TaskSection {
        return try {
            TaskSection.valueOf(value)
        } catch (e: IllegalArgumentException) {
            TaskSection.IMMEDIATE
        }
    }
}
