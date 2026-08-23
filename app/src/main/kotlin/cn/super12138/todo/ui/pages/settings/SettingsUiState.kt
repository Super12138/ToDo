package cn.super12138.todo.ui.pages.settings

import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.model.ContrastLevel
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.PaletteStyle
import cn.super12138.todo.logic.model.SortingMethod

data class SettingsAppearanceUiState(
    val dynamicColor: Boolean = Constants.PREF_DYNAMIC_COLOR_DEFAULT,
    val paletteStyle: PaletteStyle = PaletteStyle.TonalSpot,
    val darkMode: DarkMode = DarkMode.FollowSystem,
    val pureBlackMode: Boolean = Constants.PREF_PURE_BLACK_MODE_DEFAULT,
    val contrastLevel: ContrastLevel = ContrastLevel.Default,
    val previewColorSystem: Boolean = Constants.PREF_PREVIEW_COLOR_SYSTEM_DEFAULT
)

data class SettingsInterfaceUiState(
    val sortingMethod: SortingMethod = SortingMethod.Sequential,
    val textFieldAutoFocus: Boolean = Constants.PREF_TEXT_FIELD_AUTO_FOCUS_DEFAULT,
    val secureMode: Boolean = Constants.PREF_SECURE_MODE_DEFAULT,
    val hapticFeedback: Boolean = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT
)

data class SettingsDataUiState(
    val categories: List<String> = emptyList()
)
