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
}