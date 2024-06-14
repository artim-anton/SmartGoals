package com.artimanton.smartgoals.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    val sessionGoalId: Int,
    val relatedToGoal: String,
    val date: Long,
    val duration: Long,
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int? = null
)
