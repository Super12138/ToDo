package cn.super12138.todo.utils

import androidx.compose.runtime.mutableStateOf

class ConfettiController {
    val visible = mutableStateOf(false)

    fun setVisibility(show: Boolean) {
        visible.value = show
    }
}