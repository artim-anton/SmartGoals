package com.artimanton.smartgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.artimanton.smartgoals.ui.theme.SmartGoalsTheme
import com.artimanton.smartgoals.model.Session
import com.artimanton.smartgoals.model.Goal
import com.artimanton.smartgoals.model.Task
import com.artimanton.smartgoals.ui.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGoalsTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

val goals = listOf(
    Goal(
        name = "English",
        goalHours = 10f,
        colors = Goal.goalCardColors[0],
        goalId = 0
    ),
    Goal(
        name = "Physics",
        goalHours = 10f,
        colors = Goal.goalCardColors[1],
        goalId = 0
    ),
    Goal(
        name = "Maths",
        goalHours = 10f,
        colors = Goal.goalCardColors[2],
        goalId = 0
    ),
    Goal(
        name = "Geology",
        goalHours = 10f,
        colors = Goal.goalCardColors[3],
        goalId = 0
    ),
    Goal(
        name = "Fine Arts",
        goalHours = 10f,
        colors = Goal.goalCardColors[4],
        goalId = 0
    ),
)

val tasks = listOf(
    Task(
        title = "Prepare notes",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToGoal = "",
        isComplete = false,
        taskGoalId = 0,
        taskId = 1
    ),
    Task(
        title = "Do Homework",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToGoal = "",
        isComplete = true,
        taskGoalId = 0,
        taskId = 1
    ),
    Task(
        title = "Go Coaching",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToGoal = "",
        isComplete = false,
        taskGoalId = 0,
        taskId = 1
    ),
    Task(
        title = "Assignment",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToGoal = "",
        isComplete = false,
        taskGoalId = 0,
        taskId = 1
    ),
    Task(
        title = "Write Poem",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToGoal = "",
        isComplete = true,
        taskGoalId = 0,
        taskId = 1
    )
)

val sessions = listOf(
    Session(
        relatedToGoal = "English",
        date = 0L,
        duration = 2,
        sessionGoalId = 0,
        sessionId = 0
    ),
    Session(
        relatedToGoal = "English",
        date = 0L,
        duration = 2,
        sessionGoalId = 0,
        sessionId = 0
    ),
    Session(
        relatedToGoal = "Physics",
        date = 0L,
        duration = 2,
        sessionGoalId = 0,
        sessionId = 0
    ),
    Session(
        relatedToGoal = "Maths",
        date = 0L,
        duration = 2,
        sessionGoalId = 0,
        sessionId = 0
    ),
    Session(
        relatedToGoal = "English",
        date = 0L,
        duration = 2,
        sessionGoalId = 0,
        sessionId = 0
    )
)