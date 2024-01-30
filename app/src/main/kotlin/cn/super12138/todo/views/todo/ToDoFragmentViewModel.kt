package cn.super12138.todo.views.todo

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.model.ToDo
import kotlinx.coroutines.launch

class ToDoFragmentViewModel : ViewModel() {
    val emptyTipVis = MutableLiveData<Int>(View.GONE)
    val refreshData = MutableLiveData<Int>(0)
    val todoList = ArrayList<ToDo>()

    init {
        loadToDos()
    }

    private fun loadToDos(){
        viewModelScope.launch {
            val todos = Repository.getAllUncomplete()
            var count = 0
            for (todo in todos) {
                todoList.add(ToDo(todo.uuid, todo.content, todo.subject))
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