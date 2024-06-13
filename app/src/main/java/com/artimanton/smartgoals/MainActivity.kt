package com.artimanton.smartgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.artimanton.smartgoals.ui.dashboard.DashboardScreen
import com.artimanton.smartgoals.ui.theme.SmartGoalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGoalsTheme {
                DashboardScreen()
            }
        }
    }
}