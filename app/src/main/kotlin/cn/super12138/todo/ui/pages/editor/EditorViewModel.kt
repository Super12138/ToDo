package cn.super12138.todo.ui.pages.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.SettingsRepository
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.ui.components.ChipItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditorViewModel(
    private val settingsRepository: SettingsRepository,
    val initialTask: TaskEntity? = null,
) : ViewModel() {
    companion object {
        const val TAG = "Editor"
    }

    private val localUiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = combine(
        settingsRepository.textFieldAutoFocusFlow,
        settingsRepository.categoriesFlow,
        localUiState
    ) { textFieldAutoFocus, categories, localState ->/*
        val categoryList = categories.mapIndexed { index, category ->
            ChipItem(
                id = index,
                label = category
            )
        } + ChipItem(
            id = -1,
            label =
        )*/

        localState.copy(
            isTextFieldAutoFocus = textFieldAutoFocus,
            categoryList = categories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TaskEditorUiState()
    )

    init {
        if (initialTask != null) {
            with(initialTask) {
                localUiState.update {
                    it.copy(
                        content = content,
                        category = category,
                        priority = priority,
                        dueDate = dueDate,
                        isCompleted = isCompleted
                    )
                }
            }
        }
    }

    fun setContentText(content: String) = localUiState.update { it.copy(content = content) }
    fun setCategoryText(category: String) = localUiState.update { it.copy(content = category) }
    fun setPriority(priority: Float) = localUiState.update { it.copy(priority = priority) }
    fun setDueDate(dueDate: Long?) = localUiState.update { it.copy(dueDate = dueDate) }
    fun setCompleted(isCompleted: Boolean) =
        localUiState.update { it.copy(isCompleted = isCompleted) }

    fun showDeleteConfirmDialog() = localUiState.update { it.copy(showDeleteConfirmDialog = true) }
    fun hideDeleteConfirmDialog() = localUiState.update { it.copy(showDeleteConfirmDialog = false) }

    fun showExitConfirmDialog() = localUiState.update { it.copy(showExitConfirmDialog = true) }
    fun hideExitConfirmDialog() = localUiState.update { it.copy(showExitConfirmDialog = false) }
    fun setSelectedCategory(id: Int) = localUiState.update { it.copy(selectedCategoryId = id) }

    /*fun setTaskEntity(task: TaskEntity?) = localUiState.update {
        if (task == null) {
            // 新建模式，清空一切
            it.copy(
                initialTask = null,
                contentState = TextFieldState(),
                categoryContentState = TextFieldState(),
                selectedCategoryIndex = if (uiState.value.categoryList.size - 1 >= 1) 0 else -1,
                priorityState = 0f,
                dueDateState = null,
                isCompleted = false,
                isContentError = false,
                isCategoryError = false,
                showExitConfirmDialog = false,
                showDeleteConfirmDialog = false
            )
        } else {
            // 编辑模式，填充任务数据
            val index =

            it.copy(
                initialTask = task,
                contentState = TextFieldState(task.content),
                categoryContentState = TextFieldState(task.category),
                selectedCategoryIndex = index,
                priorityState = task.priority,
                dueDateState = task.dueDate,
                isCompleted = task.isCompleted,
                isContentError = false,
                isCategoryError = false,
                showExitConfirmDialog = false,
                showDeleteConfirmDialog = false
            )
        }
    }*/


}