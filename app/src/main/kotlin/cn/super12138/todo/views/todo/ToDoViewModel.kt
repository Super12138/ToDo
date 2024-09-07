package cn.super12138.todo.views.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.logic.model.ToDo
import kotlinx.coroutines.launch

class ToDoViewModel : ViewModel() {
    val addData = MutableLiveData<Int>(0)
    val removeData = MutableLiveData<Int>(0)
    val refreshData = MutableLiveData<Int>(0)

    val todoList = ArrayList<ToDo>()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val todos = Repository.getAllIncomplete()
            for (todo in todos) {
                todoList.add(ToDo(todo.uuid, todo.state, todo.content, todo.subject))
            }
            if (todoList.size > 0) {
                refreshData.value = 1
            }
        }
    }

    fun deleteTask(position: Int, uuid: String) {
        todoList.removeAt(position)
        viewModelScope.launch {
            Repository.deleteByUUID(uuid)
        }
    }

    fun insertTask(todo: ToDoRoom) {
        viewModelScope.launch {
            Repository.insert(todo)
        }
    }

    fun updateTaskState(uuid: String) {
        viewModelScope.launch {
            Repository.updateStateByUUID(uuid)
        }
    }

    fun updateTask(position: Int, todo: ToDoRoom) {
        todoList.removeAt(position)
        todoList.add(
            position,
            ToDo(
                todo.uuid,
                todo.state,
                todo.content,
                todo.subject
            )
        )
        viewModelScope.launch {
            Repository.update(todo)
        }
    }
}