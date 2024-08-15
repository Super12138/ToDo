package cn.super12138.todo.views.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {
    val currentPage = MutableLiveData<Int>(0)
}