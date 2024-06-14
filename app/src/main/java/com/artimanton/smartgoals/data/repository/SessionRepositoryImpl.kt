package com.artimanton.smartgoals.data.repository

import com.artimanton.smartgoals.data.local.SessionDao
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
): SessionRepository {

    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions()
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions().take(count = 5)
    }

    override fun getRecentTenSessionsForSubject(goalId: Int): Flow<List<Session>> {
        return sessionDao.getRecentSessionsForGoal(goalId).take(count = 10)
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionsDurationByGoalId(goalId: Int): Flow<Long> {
        return sessionDao.getTotalSessionsDurationByGoalId(goalId)
    }
}