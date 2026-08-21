package cn.super12138.todo.ui.pages.editor

data class TaskEditorUiState(
    val content: String = "",
    val category: String = "",
    val selectedCategoryId: Int = -1,
    val priority: Float = 0f,
    val dueDate: Long? = null,
    val isCompleted: Boolean = false,
    val categoryList: List<String> = emptyList(),
    val isTextFieldAutoFocus: Boolean = false,
    val showExitConfirmDialog: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false
) {
    /*fun isModified(): Boolean {
        Log.d(
            "Editor",
            "UiState: Original: content=${initialTask?.content}, category=${initialTask?.category}, priority=${initialTask?.priority}, isCompleted=${initialTask?.isCompleted}, dueDate=${initialTask?.dueDate}"
        )
        Log.d(
            "Editor",
            "UiState: Current: content=${content.text}, category=${category.text}, priority=${priority}, isCompleted=${isCompleted}, dueDate=${dueDate}"
        )
        var isModified = false
        if ((initialTask?.content ?: "") != content.text.toString()) isModified = true
        if ((initialTask?.category ?: "") != category.text.toString()) isModified = true
        if ((initialTask?.priority ?: 0f) != priority) isModified = true
        if ((initialTask?.isCompleted == true) != isCompleted) isModified = true
        if (initialTask?.dueDate != dueDate) isModified = true
        return isModified
    }*/

    /*fun getNewTaskEntity(): TaskEntity {
        return TaskEntity(
            id = initialTask?.id ?: 0,
            content = taskContentState.text.toString(),
            category = categoryContentState.text.toString(),
            priority = priorityState,
            isCompleted = isCompleted,
            dueDate = dueDateState
        )
    }*/
}
