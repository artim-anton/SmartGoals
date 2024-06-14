package com.artimanton.smartgoals.ui.session

import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Session

data class SessionState(
    val goals: List<Goal> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToGoal: String? = null,
    val goalId: Int? = null,
    val session: Session? = null
)
