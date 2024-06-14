package com.artimanton.smartgoals.ui.dashboard

import androidx.compose.ui.graphics.Color
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.model.Task

sealed class DashboardEvent {
    data object SaveGoal : DashboardEvent()
    data object DeleteSession : DashboardEvent()
    data class OnDeleteSessionButtonClick(val session: Session): DashboardEvent()
    data class OnTaskIsCompleteChange(val task: Task): DashboardEvent()
    data class OnGoalCardColorChange(val colors: List<Color>): DashboardEvent()
    data class OnSubjectNameChange(val name: String): DashboardEvent()
    data class OnGoalStudyHoursChange(val hours: String): DashboardEvent()
}
