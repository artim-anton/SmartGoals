package com.artimanton.smartgoals.domain.repository

import com.artimanton.smartgoals.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    suspend fun upsertGoal(goal: Goal)

    fun getTotalGoalCount(): Flow<Int>

    fun getTotalGoalHours(): Flow<Float>

    suspend fun deleteGoal(goalId: Int)

    suspend fun getGoalById(goalId: Int): Goal?

    fun getAllGoal(): Flow<List<Goal>>
}