package cn.super12138.todo.views.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.model.ToDo
import kotlinx.coroutines.launch

class AllTasksViewModel : ViewModel() {
    val todoListAll = ArrayList<ToDo>()
    val refreshData = MutableLiveData<Int>(0)

    init {
        loadToDos()
    }

    private fun loadToDos() {
        viewModelScope.launch {
            val todos = Repository.getAll()
            for (todo in todos) {
                todoListAll.add(ToDo(todo.uuid, todo.state, todo.content, todo.subject))
            }
            if (todoListAll.size > 0) {
                refreshData.value = 1
            }
        }
    }
}