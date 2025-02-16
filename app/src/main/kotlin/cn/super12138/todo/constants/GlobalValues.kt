package cn.super12138.todo.constants

import cn.super12138.todo.utils.SPDelegates

object GlobalValues {
    var dynamicColor: Boolean by SPDelegates(
        key = Constants.PREF_DYNAMIC_COLOR,
        default = Constants.PREF_DYNAMIC_COLOR_DEFAULT
    )
    var paletteStyle: Int by SPDelegates(
        key = Constants.PREF_PALETTE_STYLE,
        default = Constants.PREF_PALETTE_STYLE_DEFAULT
    )
    var darkMode: Int by SPDelegates(
        key = Constants.PREF_DARK_MODE,
        default = Constants.PREF_DARK_MODE_DEFAULT
    )
    var contrastLevel: Float by SPDelegates(
        key = Constants.PREF_CONTRAST_LEVEL,
        default = Constants.PREF_CONTRAST_LEVEL_DEFAULT
    )
    var showCompleted: Boolean by SPDelegates(
        key = Constants.PREF_SHOW_COMPLETED,
        default = Constants.PREF_SHOW_COMPLETED_DEFAULT
    )
    var sortingMethod: Int by SPDelegates(
        key = Constants.PREF_SORTING_METHOD,
        default = Constants.PREF_SORTING_METHOD_DEFAULT
    )
    var secureMode: Boolean by SPDelegates(
        key = Constants.PREF_SECURE_MODE,
        default = Constants.PREF_SECURE_MODE_DEFAULT
    )
    var hapticFeedback: Boolean by SPDelegates(
        key = Constants.PREF_HAPTIC_FEEDBACK,
        default = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT
    )
}