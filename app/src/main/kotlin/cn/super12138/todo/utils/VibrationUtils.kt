package cn.super12138.todo.utils

import android.view.HapticFeedbackConstants
import android.view.View
import cn.super12138.todo.constant.GlobalValues

object VibrationUtils {
    fun performHapticFeedback(
        view: View?,
        hapticFeedbackConstants: Int = HapticFeedbackConstants.KEYBOARD_TAP
    ) {
        if (GlobalValues.hapticFeedback) {
            view?.performHapticFeedback(hapticFeedbackConstants)
        }
    }
}