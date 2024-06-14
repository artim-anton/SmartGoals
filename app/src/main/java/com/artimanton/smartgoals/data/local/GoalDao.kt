package com.artimanton.smartgoals.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.artimanton.smartgoals.domain.model.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Upsert
    suspend fun upsertGoal(subject: Goal)

    @Query("SELECT COUNT(*) FROM GOAL")
    fun getTotalGoalCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM GOAL")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM Goal WHERE goalId = :goalId")
    suspend fun getGoalById(goalId: Int): Goal?

    @Query("DELETE FROM Goal WHERE goalId = :goalId")
    suspend fun deleteGoal(goalId: Int)

    @Query("SELECT * FROM Goal")
    fun getAllGoals(): Flow<List<Goal>>
}