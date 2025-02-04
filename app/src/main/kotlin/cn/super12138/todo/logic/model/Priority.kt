package cn.super12138.todo.logic.model

import android.content.Context
import cn.super12138.todo.R

enum class Priority(val value: Float) {
    Urgent(10f),
    Important(5f),
    Default(0f),
    NotImportant(-5f),
    NotUrgent(-10f);

    fun getDisplayName(context: Context): String {
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
        fun fromFloat(float: Float): Priority {
            return Priority.entries.find { it.value == float } ?: Default
        }
    }
}