package com.artimanton.smartgoals.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.artimanton.smartgoals.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM Task WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("DELETE FROM Task WHERE taskGoalId = :goalId")
    suspend fun deleteTasksByGoalId(goalId: Int)

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM Task WHERE taskGoalId = :goalId")
    fun getTasksForGoal(goalId: Int): Flow<List<Task>>

    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>
}