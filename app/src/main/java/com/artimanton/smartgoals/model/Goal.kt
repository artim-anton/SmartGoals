package com.artimanton.smartgoals.model

import androidx.compose.ui.graphics.Color
import com.artimanton.smartgoals.ui.theme.gradient1
import com.artimanton.smartgoals.ui.theme.gradient2
import com.artimanton.smartgoals.ui.theme.gradient3
import com.artimanton.smartgoals.ui.theme.gradient4
import com.artimanton.smartgoals.ui.theme.gradient5

data class Goal(
    val name: String,
    val goalHours: Float,
    val colors: List<Color>,
    val subjectId: Int
) {
    companion object {
        val goalCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
