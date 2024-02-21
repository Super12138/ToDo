package cn.super12138.todo.views.all

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.model.ToDo
import kotlinx.coroutines.launch

class AllTasksViewModel : ViewModel() {
    val emptyTipVis = MutableLiveData<Int>(View.VISIBLE)
    val todoListAll = ArrayList<ToDo>()

    init {
        loadToDos()
    }

    private fun loadToDos() {
        viewModelScope.launch {
            val todos = Repository.getAll()
            var count = 0
            for (todo in todos) {
                todoListAll.add(ToDo(todo.uuid, todo.state, todo.content, todo.subject))
                count++
            }
            if (count == 0) {
                emptyTipVis.value = View.VISIBLE
            } else {
                emptyTipVis.value = View.GONE
            }
        }
    }
}