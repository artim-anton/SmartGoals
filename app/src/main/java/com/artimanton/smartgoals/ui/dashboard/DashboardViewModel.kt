package com.artimanton.smartgoals.ui.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artimanton.smartgoals.domain.model.Goal
import com.artimanton.smartgoals.domain.model.Session
import com.artimanton.smartgoals.domain.model.Task
import com.artimanton.smartgoals.domain.repository.GoalRepository
import com.artimanton.smartgoals.domain.repository.SessionRepository
import com.artimanton.smartgoals.domain.repository.TaskRepository
import com.artimanton.smartgoals.util.SnackBarEvent
import com.artimanton.smartgoals.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        goalRepository.getTotalGoalCount(),
        goalRepository.getTotalGoalHours(),
        goalRepository.getAllGoal(),
        sessionRepository.getTotalSessionsDuration()
    ) { state, goalCount, goalHours, goal, totalSessionDuration ->
        state.copy(
            totalGoalCount = goalCount,
            totalGoalDoneHours = goalHours,
            goals = goal,
            totalGoalHours = totalSessionDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    val recentSessions: StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(goalName = event.name)
                }
            }

            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is DashboardEvent.OnGoalCardColorChange -> {
                _state.update {
                    it.copy(goalCardColors = event.colors)
                }
            }

            is DashboardEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }

            DashboardEvent.SaveGoal -> saveGoal()
            DashboardEvent.DeleteSession -> {}
            is DashboardEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Saved in completed tasks.")
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        "Couldn't update task. ${e.message}",
                        SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun saveGoal() {
        viewModelScope.launch {
            try {
                goalRepository.upsertGoal(
                    goal = Goal(
                        name = state.value.goalName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.goalCardColors.map { it.toArgb() }
                    )
                )
                _state.update {
                    it.copy(
                        goalName = "",
                        goalStudyHours = "",
                        goalCardColors = Goal.goalCardColors.random()
                    )
                }
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(message = "Goal saved successfully")
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Couldn't save goal. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

}