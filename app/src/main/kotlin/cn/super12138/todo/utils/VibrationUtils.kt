package cn.super12138.todo.utils

import android.view.HapticFeedbackConstants
import android.view.View
import cn.super12138.todo.constants.GlobalValues

object VibrationUtils {
    fun performHapticFeedback(
        view: View,
        feedbackConstants: Int = HapticFeedbackConstants.CONTEXT_CLICK
    ) {
        if (GlobalValues.hapticFeedback) {
            view.performHapticFeedback(feedbackConstants)
        }
    }
}