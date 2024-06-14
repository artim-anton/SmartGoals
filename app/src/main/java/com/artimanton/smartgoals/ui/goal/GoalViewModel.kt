package com.example.studysmart.presentation.subject

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Task
import com.artimanton.smartgoals.domain.repository.GoalRepository
import com.artimanton.smartgoals.domain.repository.SessionRepository
import com.artimanton.smartgoals.domain.repository.TaskRepository
import com.artimanton.smartgoals.ui.goal.GoalEvent
import com.artimanton.smartgoals.ui.goal.GoalScreenNavArgs
import com.artimanton.smartgoals.ui.goal.GoalState
import com.artimanton.smartgoals.ui.navArgs
import com.artimanton.smartgoals.util.SnackBarEvent
import com.artimanton.smartgoals.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: GoalScreenNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(GoalState())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForGoal(navArgs.goalId),
        taskRepository.getCompletedTasksForGoal(navArgs.goalId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.goalId),
        sessionRepository.getTotalSessionsDurationByGoalId(navArgs.goalId)
    ) { state, upcomingTasks, completedTask, recentSessions, totalSessionsDuration ->
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTask,
            recentSessions = recentSessions,
            studiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = GoalState()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    init {
        fetchGoal()
    }

    fun onEvent(event: GoalEvent) {
        when (event) {
            is GoalEvent.OnGoalCardColorChange -> {
                _state.update {
                    it.copy(goalCardColors = event.color)
                }
            }

            is GoalEvent.OnGoalNameChange -> {
                _state.update {
                    it.copy(goalName = event.name)
                }
            }

            is GoalEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is GoalEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is GoalEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }

            GoalEvent.UpdateGoal -> updateGoal()
            GoalEvent.DeleteGoal -> deleteGoal()
            GoalEvent.DeleteSession -> deleteSession()

            GoalEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }
        }
    }

    private fun updateGoal() {
        viewModelScope.launch {
            try {
                goalRepository.upsertGoal(
                    goal = Goal(
                        goalId = state.value.currentGoalId,
                        name = state.value.goalName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.goalCardColors.map { it.toArgb() }
                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Goal updated successfully.")
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't update goal. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun fetchGoal() {
        viewModelScope.launch {
            goalRepository
                .getGoalById(navArgs.goalId)?.let { goal ->
                    _state.update {
                        it.copy(
                            goalName = goal.name,
                            goalStudyHours = goal.goalHours.toString(),
                            goalCardColors = goal.colors.map { colors -> Color(colors) },
                            currentGoalId = goal.goalId
                        )
                    }
                }
        }
    }

    private fun deleteGoal() {
        viewModelScope.launch {
            try {
                val currentGoalId = state.value.currentGoalId
                if (currentGoalId != null) {
                    withContext(Dispatchers.IO) {
                        goalRepository.deleteGoal(goalId = currentGoalId)
                    }
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Goal deleted successfully")
                    )
                    _snackBarEventFlow.emit(SnackBarEvent.NavigateUp)
                } else {
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "No Goal to delete")
                    )
                }
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't delete goal. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                if (task.isComplete) {
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Saved in upcoming tasks.")
                    )
                } else {
                    _snackBarEventFlow.emit(
                        SnackBarEvent.ShowSnackBar(message = "Saved in completed tasks.")
                    )
                }
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't update task. ${e.message}",
                        duration = SnackbarDuration.Long
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

}