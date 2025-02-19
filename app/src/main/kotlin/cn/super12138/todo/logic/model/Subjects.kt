package cn.super12138.todo.logic.model

import android.content.Context
import cn.super12138.todo.R

enum class Subjects(val id: Int) {
    Chinese(0),
    Math(1),
    English(2),
    Biology(3),
    Geography(4),
    Physics(5),
    Moral(6),
    Chemistry(7),
    History(8),
    Others(99),
    Custom(100);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            Chinese -> R.string.subject_chinese
            Math -> R.string.subject_math
            English -> R.string.subject_english
            Biology -> R.string.subject_biology
            Geography -> R.string.subject_geography
            Physics -> R.string.subject_physics
            Moral -> R.string.subject_moral
            Chemistry -> R.string.subject_chemistry
            History -> R.string.subject_history
            Others -> R.string.subject_others
            Custom -> R.string.subject_customization
        }
        return context.getString(resId) // 返回资源中的文本
    }

    companion object {
        // 根据 ID 获取 Subjects
        fun fromId(id: Int): Subjects {
            return entries.find { it.id == id } ?: Others
        }
    }
}