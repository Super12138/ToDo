package cn.super12138.todo.ui.pages.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.SettingsRepository
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.ui.components.ChipItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditorViewModel(
    val initialTask: TaskEntity?,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val localUiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = combine(
        settingsRepository.textFieldAutoFocusFlow,
        settingsRepository.categoriesFlow,
        localUiState
    ) { textFieldAutoFocus, categories, localState ->
        localState.copy(
            shouldAutoFocusContent = textFieldAutoFocus,
            categoryList = categories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskEditorUiState()
    )

    init {
        if (initialTask != null) {
            with(initialTask) {
                localUiState.update {
                    it.copy(
                        content = content,
                        category = category,
                        priority = Priority.fromFloat(priority),
                        dueDateMillis = dueDate,
                        isCompleted = isCompleted
                    )
                }
            }
        }
    }

    fun setContentText(content: String) = localUiState.update { it.copy(content = content) }
    fun setCategoryText(category: String) = localUiState.update { it.copy(category = category) }
    fun setPriority(priority: Priority) = localUiState.update { it.copy(priority = priority) }
    fun setDueDate(dueDate: Long?) = localUiState.update { it.copy(dueDateMillis = dueDate) }
    fun setCompleted(isCompleted: Boolean) =
        localUiState.update { it.copy(isCompleted = isCompleted) }

    fun showDeleteConfirmDialog() = localUiState.update { it.copy(showDeleteConfirmDialog = true) }
    fun hideDeleteConfirmDialog() = localUiState.update { it.copy(showDeleteConfirmDialog = false) }

    fun showExitConfirmDialog() = localUiState.update { it.copy(showExitConfirmDialog = true) }
    fun hideExitConfirmDialog() = localUiState.update { it.copy(showExitConfirmDialog = false) }
    fun setSelectedCategory(chipItem: ChipItem?) {
        if (chipItem == null) return
        localUiState.update {
            it.copy(
                selectedCategoryId = chipItem.id,
                category = if (chipItem.id == -1) it.category else chipItem.label
            )
        }
    }

    fun setSelectedCategoryId(id: Int) = localUiState.update { it.copy(selectedCategoryId = id) }
    fun getNewTaskEntity(): TaskEntity {
        with(uiState.value) {
            return TaskEntity(
                content = content,
                category = category,
                isCompleted = isCompleted,
                priority = priority.value,
                dueDate = dueDateMillis,
                id = initialTask?.id ?: 0
            )
        }
    }
}