package cn.super12138.todo.logic.model

import android.content.Context
import cn.super12138.todo.R

enum class SortingMethod(val id: Int) {
    // 按添加先后顺序
    Sequential(1),

    // 按学科
    Category(2),

    // 按优先级
    Priority(3),

    // 按完成情况
    Completion(4),

    // 按字母升序
    AlphabeticalAscending(5),

    // 按字母降序
    AlphabeticalDescending(6);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            Sequential -> R.string.sorting_sequential
            Category -> R.string.sorting_category
            Priority -> R.string.sorting_priority
            Completion -> R.string.sorting_completion
            AlphabeticalAscending -> R.string.sorting_alphabetical_ascending
            AlphabeticalDescending -> R.string.sorting_alphabetical_descending
        }
        return context.getString(resId)
    }

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id } ?: Sequential
    }
}