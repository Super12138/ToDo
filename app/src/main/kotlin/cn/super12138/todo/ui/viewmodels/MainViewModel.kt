package cn.super12138.todo.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.database.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val toDos: Flow<List<TodoEntity>> = Repository.getAllTodos()
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
}