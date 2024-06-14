package com.artimanton.smartgoals.domain.repository

import com.artimanton.smartgoals.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(taskId: Int)

    suspend fun getTaskById(taskId: Int): Task?

    fun getUpcomingTasksForGoal(goalInt: Int): Flow<List<Task>>

    fun getCompletedTasksForGoal(goalInt: Int): Flow<List<Task>>

    fun getAllUpcomingTasks(): Flow<List<Task>>
}