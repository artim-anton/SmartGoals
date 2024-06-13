package com.artimanton.smartgoals.model

data class Task(
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToGoal: String,
    val isComplete: Boolean,
    val taskGoalId: Int,
    val taskId: Int
)
