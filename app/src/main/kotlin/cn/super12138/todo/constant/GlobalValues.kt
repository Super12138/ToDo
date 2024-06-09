package cn.super12138.todo.constant

import cn.super12138.todo.utils.SPDelegates

object GlobalValues {
    var darkMode: String by SPDelegates(Constants.PREF_DARK_MODE, "0")
    var devMode: Boolean by SPDelegates(Constants.PREF_DEV_MODE, false)
    var springFestivalTheme: Boolean by SPDelegates(Constants.PREF_SPRING_FESTIVAL_THEME, false)
    var secureMode: Boolean by SPDelegates(Constants.PREF_SECURE_MODE, false)
    var hapticFeedback: Boolean by SPDelegates(Constants.PREF_HAPTIC_FEEDBACK, true)
}