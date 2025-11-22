package cn.super12138.todo.logic.model

import android.content.Context
import cn.super12138.todo.R

enum class Priority(val value: Float) {
    Urgent(2f),
    Important(1f),
    Default(0f),
    NotImportant(-1f),
    NotUrgent(-2f);

    fun displayName(context: Context): String {
        val resId = when (this) {
            Urgent -> R.string.priority_urgent
            Important -> R.string.priority_important
            Default -> R.string.priority_default
            NotImportant -> R.string.priority_not_important
            NotUrgent -> R.string.priority_not_urgent
        }
        return context.getString(resId)
    }

    companion object {
        fun fromFloat(float: Float) = entries.find { it.value == float } ?: Default
    }
}