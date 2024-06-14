package com.artimanton.smartgoals.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.model.Task

@Database(
    entities = [Goal::class, Session::class, Task::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun goalDao(): GoalDao
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
}