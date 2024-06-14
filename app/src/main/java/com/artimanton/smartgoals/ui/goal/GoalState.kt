package com.artimanton.smartgoals.ui.goal

import androidx.compose.ui.graphics.Color
import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.model.Task

data class GoalState(
    val currentGoalId: Int? = null,
    val goalName: String = "",
    val goalStudyHours: String = "",
    val goalCardColors: List<Color> = Goal.goalCardColors.random(),
    val studiedHours: Float = 0f,
    val progress: Float = 0f,
    val recentSessions: List<Session> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val session: Session? = null
)
