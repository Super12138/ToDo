package cn.super12138.todo.ui.pages.tasks

import cn.super12138.todo.logic.database.TaskEntity

data class TasksPageUiState(
    val originalTaskList: List<TaskEntity> = emptyList(),
    val selectedTaskIds: Set<Int> = emptySet(),
    val inSearchMode: Boolean = false,
    val inSelectionMode: Boolean = false,
    val searchQuery: String = "",
    val showDeleteConfirmDialog: Boolean = false
)