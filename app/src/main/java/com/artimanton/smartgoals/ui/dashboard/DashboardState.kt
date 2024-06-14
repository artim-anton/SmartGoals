package com.artimanton.smartgoals.ui.dashboard

import androidx.compose.ui.graphics.Color
import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Session

data class DashboardState(
    val totalGoalCount: Int = 0,
    val totalGoalHours: Float = 0f,
    val totalGoalDoneHours: Float = 0f,
    val goals: List<Goal> = emptyList(),
    val goalName: String = "",
    val goalStudyHours: String = "",
    val goalCardColors: List<Color> = Goal.goalCardColors.random(),
    val session: Session? = null
)
