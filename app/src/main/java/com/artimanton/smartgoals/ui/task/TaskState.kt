package com.artimanton.smartgoals.ui.task

import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.util.Priority

data class TaskState(
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val isTaskComplete: Boolean = false,
    val priority: Priority = Priority.LOW,
    val relatedToGoal: String? = null,
    val goals: List<Goal> = emptyList(),
    val goalId: Int? = null,
    val currentTaskId: Int? = null
)
