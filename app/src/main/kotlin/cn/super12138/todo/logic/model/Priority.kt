package cn.super12138.todo.logic.model

import androidx.annotation.StringRes
import cn.super12138.todo.R

enum class Priority(
    val value: Float,
    @param:StringRes val nameRes: Int
) {
    NotUrgent(value = -2f, nameRes = R.string.priority_not_urgent),
    NotImportant(value = -1f, nameRes = R.string.priority_not_important),
    Default(value = 0f, nameRes = R.string.priority_default),
    Important(value = 1f, nameRes = R.string.priority_important),
    Urgent(value = 2f, nameRes = R.string.priority_urgent);

    companion object {
        fun fromFloat(float: Float) = entries.find { it.value == float } ?: Default
    }
}