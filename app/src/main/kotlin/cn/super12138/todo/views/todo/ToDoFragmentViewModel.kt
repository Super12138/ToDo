package cn.super12138.todo.views.todo

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDoFragmentViewModel : ViewModel() {
    val emptyTipVis = MutableLiveData<Int>(View.GONE)
    val refreshData = MutableLiveData<Int>(0)
}