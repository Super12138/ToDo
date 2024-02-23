package cn.super12138.todo.utils

import android.content.Context
import android.widget.Toast
import cn.super12138.todo.ToDoApp

fun String.showToast(
    context: Context = ToDoApp.context,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, this, duration).show()
}

fun Int.showToast(context: Context = ToDoApp.context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}