package cn.super12138.todo.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import cn.super12138.todo.logic.model.Priority

@Composable
@Stable
fun Priority.containerColor(): Color =
    when (this) {
        Priority.NotUrgent -> MaterialTheme.colorScheme.surfaceContainerHighest
        Priority.NotImportant -> MaterialTheme.colorScheme.surfaceContainerHighest
        Priority.Default -> MaterialTheme.colorScheme.secondary
        Priority.Important -> MaterialTheme.colorScheme.tertiary
        Priority.Urgent -> MaterialTheme.colorScheme.error
    }