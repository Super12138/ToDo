package cn.super12138.todo.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.constants.GlobalValues
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.ContrastLevel
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.SortingMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // 待办
    private val toDos: Flow<List<TodoEntity>> = Repository.getAllTodos()
    var appSortingMethod by mutableStateOf(SortingMethod.fromId(GlobalValues.sortingMethod))
    val sortedTodos: Flow<List<TodoEntity>> = toDos.map { list ->
        when (appSortingMethod) {
            SortingMethod.Date -> list.sortedBy { it.id }
            SortingMethod.Priority -> list.sortedByDescending { it.priority } // 优先级高的在前
            SortingMethod.Completion -> list.sortedBy { it.isCompleted } // 未完成的在前
            SortingMethod.AlphabeticalAscending -> list.sortedBy { it.content }
            SortingMethod.AlphabeticalDescending -> list.sortedByDescending { it.content }
        }
    }
    val showConfetti = mutableStateOf(false)
    var selectedEditTodo by mutableStateOf<TodoEntity?>(null)
        private set
    var showCompletedTodos by mutableStateOf(GlobalValues.showCompleted)

    // 深色模式
    var appDarkMode by mutableStateOf(DarkMode.fromId(GlobalValues.darkMode))
        private set
    var appContrastLevel by mutableStateOf(ContrastLevel.fromFloat(GlobalValues.contrastLevel))
        private set

    // 多选逻辑参考：https://github.com/X1nto/Mauth
    private val _selectedTodoIds = MutableStateFlow(listOf<Int>())
    val selectedTodoIds = _selectedTodoIds.asStateFlow()

    fun addTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.insertTodo(toDo)
        }
    }

    fun updateTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.updateTodo(toDo)
        }
    }

    fun deleteTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.deleteTodo(toDo)
        }
    }

    /*fun deleteAllTodo() {
        viewModelScope.launch {
            Repository.deleteAllTodo()
        }
    }*/

    fun setEditTodoItem(toDo: TodoEntity?) {
        selectedEditTodo = toDo
    }

    /**
     * 切换待办的选择状态
     */
    fun toggleTodoSelection(toDo: TodoEntity) {
        _selectedTodoIds.update { idList ->
            if (idList.contains(toDo.id)) {
                // 若已经选择取消选择
                idList - toDo.id
            } else {
                // 若未选择添加到列表中，立即选中
                idList + toDo.id
            }
        }
    }

    /**
     * 切换是否全选
     */
    fun toggleAllSelected() {
        viewModelScope.launch {
            toDos.firstOrNull()?.let { todos ->
                val allIds = todos.map { it.id }
                _selectedTodoIds.update { currentSelectedIds ->
                    if (currentSelectedIds.containsAll(allIds)) {
                        // 如果当前是全选状态，取消所有选择（切换为全不选）
                        emptyList()
                    } else {
                        // 如果当前不是全选状态，选中所有项
                        allIds
                    }
                }
            }
        }
    }

    /**
     * 清除全部已选择的待办
     */
    fun clearAllTodoSelection() {
        _selectedTodoIds.update { emptyList() }
    }

    /**
     * 删除选择的待办
     */
    fun deleteSelectedTodo() {
        viewModelScope.launch {
            Repository.deleteTodoFromIds(selectedTodoIds.value)
            clearAllTodoSelection()
        }
    }

    fun playConfetti() {
        showConfetti.value = true
    }

    /**
     * 应用设置
     */
    fun setDarkMode(darkMode: DarkMode) {
        appDarkMode = darkMode
    }

    fun setContrastLevel(contrastLevel: ContrastLevel) {
        appContrastLevel = contrastLevel
    }

    fun setShowCompleted(show: Boolean) {
        showCompletedTodos = show
    }

    fun setSortingMethod(sortingMethod: SortingMethod) {
        appSortingMethod = sortingMethod
    }
}