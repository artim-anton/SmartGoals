package com.artimanton.smartgoals.ui.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artimanton.smartgoals.ui.components.AddGoalDialog
import com.artimanton.smartgoals.ui.components.CountCard
import com.artimanton.smartgoals.ui.components.DeleteDialog
import com.artimanton.smartgoals.ui.components.studySessionsList
import com.artimanton.smartgoals.ui.components.tasksList
import com.artimanton.smartgoals.ui.destinations.TaskScreenRouteDestination
import com.artimanton.smartgoals.ui.task.TaskScreenNavArgs
import com.artimanton.smartgoals.util.SnackBarEvent
import com.example.studysmart.presentation.subject.GoalViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class GoalScreenNavArgs(
    val goalId: Int
)

@Destination(navArgsDelegate = GoalScreenNavArgs::class)
@Composable
fun GoalScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel: GoalViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    GoalScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackBarEvent = viewModel.snackBarEventFlow,
        onBackButtonClick = { navigator.navigateUp() },
        onAddTaskButtonClick = {
            val navArg = TaskScreenNavArgs(taskId = null, goalId = state.currentGoalId)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onTaskCardClick = { taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId, goalId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalScreen(
    state: GoalState,
    onEvent: (GoalEvent) -> Unit,
    snackBarEvent: SharedFlow<SnackBarEvent>,
    onBackButtonClick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    onTaskCardClick: (Int?) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    var isEditGoalDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteGoalDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.NavigateUp -> {
                    onBackButtonClick()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.studiedHours, key2 = state.goalStudyHours) {
        onEvent(GoalEvent.UpdateProgress)
    }

    AddGoalDialog(
        isOpen = isEditGoalDialogOpen,
        goalName = state.goalName,
        goalHours = state.goalStudyHours,
        onGoalNameChange = { onEvent(GoalEvent.OnGoalNameChange(it)) },
        onGoalHoursChange = { onEvent(GoalEvent.OnGoalStudyHoursChange(it)) },
        selectedColors = state.goalCardColors,
        onColorChange = { onEvent(GoalEvent.OnGoalCardColorChange(it)) },
        onDismissRequest = { isEditGoalDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(GoalEvent.UpdateGoal)
            isEditGoalDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteGoalDialogOpen,
        title = "Delete Goal?",
        bodyText = "Are you sure, you want to delete this Goal? All related " +
                "tasks and study sessions will be permanently removed. This action can not be undone",
        onDismissRequest = { isDeleteGoalDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(GoalEvent.DeleteGoal)
            isDeleteGoalDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? Your goal hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(GoalEvent.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GoalScreenTopBar(
                title = state.goalName,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isDeleteGoalDialogOpen = true },
                onEditButtonClick = { isEditGoalDialogOpen = true },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
                text = { Text(text = "Add Task") },
                expanded = isFABExpanded
            )
        }
    ) { paddingValue ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            item {
                GoalOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = state.studiedHours.toString(),
                    goalHours = state.goalStudyHours,
                    progress = state.progress
                )
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks.\n " +
                        "Click the + button to add new task.",
                tasks = state.upcomingTasks,
                onCheckBoxClick = { onEvent(GoalEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            tasksList(
                sectionTitle = "COMPLETED TASKS",
                emptyListText = "You don't have any completed tasks.\n " +
                        "Click the check box on completion of task.",
                tasks = state.completedTasks,
                onCheckBoxClick = { onEvent(GoalEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionsList(
                sectionTitle = "RECENT GOAL SESSIONS",
                emptyListText = "You don't have any recent goal sessions.\n " +
                        "Start a goal session to begin recording your progress.",
                sessions = state.recentSessions,
                onDeleteIconClick = {
                    isDeleteSessionDialogOpen = true
                    onEvent(GoalEvent.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalScreenTopBar(
    title: String,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = onDeleteButtonClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Goal"
                )
            }
            IconButton(onClick = onEditButtonClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Goal"
                )
            }
        }
    )
}

@Composable
private fun GoalOverviewSection(
    modifier: Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float
) {
    val percentageProgress = remember(key1 = progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Hours",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Done Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentageProgress%")
        }
    }
}