package cn.super12138.todo.ui.navigation

import androidx.navigation3.runtime.NavKey
import cn.super12138.todo.logic.database.TodoEntity
import kotlinx.serialization.Serializable

@Serializable
sealed class TodoScreen : NavKey {
    @Serializable
    data object Overview : TodoScreen()

    @Serializable
    data object Tasks : TodoScreen()

    @Serializable
    data class Editor(val toDo: TodoEntity?) : TodoScreen()

    @Serializable
    data object Settings : TodoScreen()

    @Serializable
    data object SettingsAppearance : TodoScreen()

    @Serializable
    data object SettingsInterface : TodoScreen()

    @Serializable
    data object SettingsData : TodoScreen()

    @Serializable
    data object SettingsDataCategory : TodoScreen()

    @Serializable
    data object SettingsAbout : TodoScreen()

    // @Serializable
    // data object SettingsAboutSpecial : TodoScreen()

    @Serializable
    data object SettingsAboutLicence : TodoScreen()

    @Serializable
    data object SettingsDev : TodoScreen()

    @Serializable
    data object SettingsDevPadding : TodoScreen()
}
