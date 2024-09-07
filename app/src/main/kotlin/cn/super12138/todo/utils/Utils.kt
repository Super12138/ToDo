package cn.super12138.todo.utils

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import cn.super12138.todo.ToDoApp

private var clickInterval = 380L
private var lastTime = 0L

/**
 * 延迟点击
 */
fun View.setOnIntervalClickListener(onIntervalClickListener: (View) -> Unit) {
    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTime > clickInterval) {
            lastTime = SystemClock.elapsedRealtime()
            onIntervalClickListener.invoke(it)
        }
    }
}

/**
 * Toast
 */
fun String.showToast(
    context: Context = ToDoApp.context,
    duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, this, duration).show()
}

fun Int.showToast(context: Context = ToDoApp.context, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).show()
}