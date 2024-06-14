package com.artimanton.smartgoals.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToGoal: String,
    val isComplete: Boolean,
    val taskGoalId: Int,
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null
)
