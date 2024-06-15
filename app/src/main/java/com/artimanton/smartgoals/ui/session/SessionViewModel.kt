package com.artimanton.smartgoals.ui.session

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.repository.GoalRepository
import com.artimanton.smartgoals.domain.repository.SessionRepository
import com.artimanton.smartgoals.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    goalRepository: GoalRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    val state = combine(
        _state,
        goalRepository.getAllGoal(),
        sessionRepository.getAllSessions()
    ) { state, goals, sessions ->
        state.copy(
            goals = goals,
            sessions = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SessionState()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event: SessionEvent) {
        when (event) {
            SessionEvent.NotifyToUpdateGoal -> notifyToUpdateGoal()
            SessionEvent.DeleteSession -> deleteSession()
            is SessionEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SessionEvent.OnRelatedGoalChange -> {
                _state.update {
                    it.copy(
                        relatedToGoal = event.goal.name,
                        goalId = event.goal.goalId
                    )
                }
            }

            is SessionEvent.SaveSession -> insertSession(event.duration)
            is SessionEvent.UpdateGoalIdAndRelatedGoal -> {
                _state.update {
                    it.copy(
                        relatedToGoal = event.relatedToGoal,
                        goalId = event.goalId
                    )
                }
            }
        }
    }

    private fun notifyToUpdateGoal() {
        viewModelScope.launch {
            if (state.value.goalId == null || state.value.relatedToGoal == null) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Please select goal related to the session."
                    )
                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let {
                    sessionRepository.deleteSession(it)
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Session deleted successfully")
                    )
                }
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun insertSession(duration: Long) {
        viewModelScope.launch {
            if (duration < 36) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Single session can not be less than 36 seconds"
                    )
                )
                return@launch
            }
            try {
                sessionRepository.insertSession(
                    session = Session(
                        sessionGoalId = state.value.goalId ?: -1,
                        relatedToGoal = state.value.relatedToGoal ?: "",
                        date = Instant.now().toEpochMilli(),
                        duration = duration
                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Session saved successfully")
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }


}