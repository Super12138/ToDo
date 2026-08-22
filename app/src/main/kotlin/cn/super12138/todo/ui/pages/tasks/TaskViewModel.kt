package cn.super12138.todo.ui.pages.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.SettingsRepository
import cn.super12138.todo.logic.TaskRepository
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.ScreenMode
import cn.super12138.todo.utils.ConfettiController
import cn.super12138.todo.utils.sort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val settingsRepository: SettingsRepository,
    private val confettiController: ConfettiController
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksPageUiState())
    val uiState: StateFlow<TasksPageUiState> = combine(
        taskRepository.getAllTasks(),
        settingsRepository.sortingMethodFlow,
        _uiState
    ) { taskList, sortingMethod, _uiState ->
        val sortedList = taskList.sort(sortingMethod)
        _uiState.copy(originalTaskList = sortedList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksPageUiState()
    )

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    /**
     * 切换待办的选择状态
     */
    fun toggleTaskSelection(task: TaskEntity) {
        _uiState.update {
            val newIds = if (it.selectedTaskIds.contains(task.id)) { // 已选择的Id里包含切换选择状态的Id
                it.selectedTaskIds - task.id // 那么就给他删了
            } else {
                it.selectedTaskIds + task.id // 不然给他加上
            }
            val newMode = if (newIds.isEmpty()) {
                // 如果之前是搜索模式，回到搜索模式，否则回到普通模式
                if (uiState.value.searchQuery.isNotEmpty()) ScreenMode.Search else ScreenMode.Default
            } else {
                ScreenMode.Selection
            }
            it.copy(selectedTaskIds = newIds, screenMode = newMode)
        }
    }

    /**
     * 切换是否全选
     */
    fun selectVisibleAllTask(taskList: List<TaskEntity>) {
        val allIds = taskList.map { it.id }.toSet()
        _uiState.update { it.copy(selectedTaskIds = allIds) }
    }

    /**
     * 清除全部已选择的待办
     */
    fun clearAllTaskSelection() = _uiState.update { it.copy(selectedTaskIds = emptySet()) }

    /**
     * 删除选择的待办
     */
    fun deleteSelectedTask() {
        viewModelScope.launch {
            taskRepository.deleteTaskFromIds(uiState.value.selectedTaskIds)
            clearAllTaskSelection()
        }
    }

    fun enterMultiSelectMode(id: Int) =
        _uiState.update {
            it.copy(
                selectedTaskIds = setOf(id),
                screenMode = ScreenMode.Selection
            )
        }

    fun exitMultiSelectMode() =
        _uiState.update {
            it.copy(
                selectedTaskIds = emptySet(),
                screenMode = ScreenMode.Default
            )
        }

    fun enterSearchMode() = _uiState.update { it.copy(screenMode = ScreenMode.Search) }
    fun exitSearchMode() = _uiState.update { it.copy(screenMode = ScreenMode.Default) }
    fun showDeleteConfirmDialog() = _uiState.update { it.copy(showDeleteConfirmDialog = true) }
    fun hideDeleteConfirmDialog() = _uiState.update { it.copy(showDeleteConfirmDialog = false) }

    fun updateSearchQuery(query: String) = _uiState.update { it.copy(searchQuery = query) }

    fun setConfettiVisibility(visible: Boolean) = confettiController.setVisibility(visible)
}