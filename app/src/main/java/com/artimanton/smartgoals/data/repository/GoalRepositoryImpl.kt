package com.artimanton.smartgoals.data.repository

import com.artimanton.smartgoals.data.local.GoalDao
import com.artimanton.smartgoals.data.local.SessionDao
import com.artimanton.smartgoals.data.local.TaskDao
import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao,
    private val taskDao: TaskDao,
    private val sessionDao: SessionDao
): GoalRepository {

    override suspend fun upsertGoal(goal: Goal) {
        goalDao.upsertGoal(goal)
    }

    override fun getTotalGoalCount(): Flow<Int> {
        return goalDao.getTotalGoalCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return goalDao.getTotalGoalHours()
    }

    override suspend fun deleteGoal(goalId: Int) {
        taskDao.deleteTasksByGoalId(goalId)
        sessionDao.deleteSessionsByGoalId(goalId)
        goalDao.deleteGoal(goalId)
    }

    override suspend fun getGoalById(goalId: Int): Goal? {
        return goalDao.getGoalById(goalId)
    }

    override fun getAllGoal(): Flow<List<Goal>> {
        return goalDao.getAllGoals()
    }
}