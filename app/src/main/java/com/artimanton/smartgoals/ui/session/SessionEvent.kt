package com.artimanton.smartgoals.ui.session

import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Session

sealed class SessionEvent {
    data class OnRelatedGoalChange(val goal: Goal) : SessionEvent()
    data class SaveSession(val duration: Long) : SessionEvent()
    data class OnDeleteSessionButtonClick(val session: Session) : SessionEvent()
    data object DeleteSession : SessionEvent()
    data object NotifyToUpdateSubject : SessionEvent()
    data class UpdateGoalIdAndRelatedGoal(
        val goalId: Int?,
        val relatedToGoal: String?
    ) : SessionEvent()
}
