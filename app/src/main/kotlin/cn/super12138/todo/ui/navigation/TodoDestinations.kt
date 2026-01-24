package cn.super12138.todo.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey

enum class TodoDestinations(
    val route: NavKey,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int
) {
    Overview(
        route = TodoScreen.Overview,
        label = cn.super12138.todo.R.string.page_overview,
        icon = cn.super12138.todo.R.drawable.ic_view_kanban,
        selectedIcon = cn.super12138.todo.R.drawable.ic_view_kanban_filled
    ),
    Tasks(
        route = TodoScreen.Tasks,
        label = cn.super12138.todo.R.string.page_tasks,
        icon = cn.super12138.todo.R.drawable.ic_ballot,
        selectedIcon = cn.super12138.todo.R.drawable.ic_ballot_filled
    ),
    Settings(
        route = TodoScreen.Settings,
        label = cn.super12138.todo.R.string.page_settings,
        icon = cn.super12138.todo.R.drawable.ic_settings,
        selectedIcon = cn.super12138.todo.R.drawable.ic_settings_filled
    )
}

