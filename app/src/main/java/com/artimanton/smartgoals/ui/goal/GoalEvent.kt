package com.artimanton.smartgoals.ui.goal

import androidx.compose.ui.graphics.Color
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.model.Task

sealed class GoalEvent {
    data object UpdateGoal : GoalEvent()
    data object DeleteGoal : GoalEvent()
    data object DeleteSession : GoalEvent()
    data object UpdateProgress : GoalEvent()
    data class OnTaskIsCompleteChange(val task: Task): GoalEvent()
    data class OnGoalCardColorChange(val color: List<Color>): GoalEvent()
    data class OnGoalNameChange(val name: String): GoalEvent()
    data class OnGoalStudyHoursChange(val hours: String): GoalEvent()
    data class OnDeleteSessionButtonClick(val session: Session): GoalEvent()
}
