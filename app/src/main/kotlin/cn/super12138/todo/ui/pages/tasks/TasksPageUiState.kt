package cn.super12138.todo.ui.pages.tasks

import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.ScreenMode

data class TasksPageUiState(
    val originalTaskList: List<TaskEntity> = emptyList(),
    val selectedTaskIds: Set<Int> = emptySet(),
    val screenMode: ScreenMode = ScreenMode.Default,
    val searchQuery: String = "",
    val showDeleteConfirmDialog: Boolean = false
)