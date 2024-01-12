package cn.super12138.todo.logic.model

data class ToDo(val uuid: String, val content: String, val subject: String) {
    var isAnimated = false
}