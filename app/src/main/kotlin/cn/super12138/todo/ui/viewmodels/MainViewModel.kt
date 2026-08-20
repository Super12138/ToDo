package cn.super12138.todo.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.TaskRepository
import cn.super12138.todo.logic.database.TaskEntity
import kotlinx.coroutines.launch

class MainViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    var showConfetti by mutableStateOf(false)
        private set

    fun setConfettiVisibility(visible: Boolean) {
        showConfetti = visible
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

}