package cn.super12138.todo.logic.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.ui.graphics.vector.ImageVector
import cn.super12138.todo.R

enum class DarkMode(
    val id: Int,
    val icon: ImageVector
) {
    FollowSystem(-1, Icons.Outlined.SettingsSuggest),
    Light(1, Icons.Outlined.LightMode),
    Dark(2, Icons.Outlined.DarkMode);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            FollowSystem -> R.string.dark_mode_system
            Light -> R.string.dark_mode_light
            Dark -> R.string.dark_mode_dark
        }
        return context.getString(resId)
    }

    companion object {
        fun fromId(id: Int): DarkMode {
            return DarkMode.entries.find { it.id == id } ?: FollowSystem
        }
    }
}