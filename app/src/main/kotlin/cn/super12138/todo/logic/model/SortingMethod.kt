package cn.super12138.todo.logic.model

import android.content.Context
import cn.super12138.todo.R

enum class SortingMethod(val id: Int) {
    Date(1),
    Priority(2),
    Completion(3),
    AlphabeticalAscending(4),
    AlphabeticalDescending(5);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            Date -> R.string.sorting_date
            Priority -> R.string.sorting_priority
            Completion -> R.string.sorting_completion
            AlphabeticalAscending -> R.string.sorting_alphabetical_ascending
            AlphabeticalDescending -> R.string.sorting_alphabetical_descending
        }
        return context.getString(resId)
    }

    companion object {
        fun fromId(id: Int): SortingMethod {
            return SortingMethod.entries.find { it.id == id } ?: Date
        }
    }
}