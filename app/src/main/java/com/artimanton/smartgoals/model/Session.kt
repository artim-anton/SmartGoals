package com.artimanton.smartgoals.model

data class Session(
    val sessionGoalId: Int,
    val relatedToGoal: String,
    val date: Long,
    val duration: Long,
    val sessionId: Int
)
