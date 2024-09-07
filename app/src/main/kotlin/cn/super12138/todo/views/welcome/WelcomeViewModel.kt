package cn.super12138.todo.views.welcome

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WelcomeViewModel : ViewModel() {
    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }

    fun increasePage() {
        _currentPage.value += 1
    }

    fun decreasePage() {
        _currentPage.value -= 1
    }
}