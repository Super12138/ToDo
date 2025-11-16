package cn.super12138.todo.ui.theme

import android.content.Context
import cn.super12138.todo.R

enum class DarkMode(
    val id: Int,
    val iconRes: Int
) {
    FollowSystem(-1, R.drawable.ic_lightbulb_2),
    Light(1, R.drawable.ic_light_mode),
    Dark(2, R.drawable.ic_dark_mode);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            FollowSystem -> R.string.dark_mode_system
            Light -> R.string.dark_mode_light
            Dark -> R.string.dark_mode_dark
        }
        return context.getString(resId)
    }

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id } ?: FollowSystem
    }
}