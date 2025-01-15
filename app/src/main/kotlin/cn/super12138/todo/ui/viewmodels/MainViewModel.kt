package cn.super12138.todo.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.database.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val toDos: Flow<List<TodoEntity>> = Repository.getAllTodos()
    var selectedEditTodoItem by mutableStateOf<TodoEntity?>(null)
        private set
    val showConfetti = mutableStateOf(false)

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

    fun playConfetti() {
        showConfetti.value = true
    }

    fun setEditTodoItem(toDo: TodoEntity?) {
        selectedEditTodoItem = toDo
    }

    fun deleteTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.deleteTodo(toDo)
        }
    }
}