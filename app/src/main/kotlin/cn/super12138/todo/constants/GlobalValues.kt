package cn.super12138.todo.constants

import cn.super12138.todo.utils.SPDelegates

object GlobalValues {
    var dynamicColor: Boolean by SPDelegates(
        Constants.PREF_DYNAMIC_COLOR,
        Constants.PREF_DYNAMIC_COLOR_DEFAULT
    )
    var paletteStyle: Int by SPDelegates(
        Constants.PREF_PALETTE_STYLE,
        Constants.PREF_PALETTE_STYLE_DEFAULT
    )
    var darkMode: Int by SPDelegates(
        Constants.PREF_DARK_MODE,
        Constants.PREF_DARK_MODE_DEFAULT
    )
    var contrastLevel: Float by SPDelegates(
        Constants.PREF_CONTRAST_LEVEL,
        Constants.PREF_CONTRAST_LEVEL_DEFAULT
    )
    var showCompleted: Boolean by SPDelegates(
        Constants.PREF_SHOW_COMPLETED,
        Constants.PREF_SHOW_COMPLETED_DEFAULT
    )
}