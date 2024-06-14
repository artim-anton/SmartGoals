package com.artimanton.smartgoals.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artimanton.smartgoals.ui.theme.gradient1
import com.artimanton.smartgoals.ui.theme.gradient2
import com.artimanton.smartgoals.ui.theme.gradient3
import com.artimanton.smartgoals.ui.theme.gradient4
import com.artimanton.smartgoals.ui.theme.gradient5

@Entity
data class Goal(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    @PrimaryKey(autoGenerate = true)
    val goalId: Int? = null
) {
    companion object {
        val goalCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
