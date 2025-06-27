package cn.super12138.todo.utils

import android.view.HapticFeedbackConstants
import android.view.View
import cn.super12138.todo.constants.Constants

object VibrationUtils {
    private var isEnabled: Boolean = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    fun performHapticFeedback(
        view: View,
        feedbackConstants: Int = HapticFeedbackConstants.CONTEXT_CLICK
    ) {
        if (isEnabled) {
            view.performHapticFeedback(feedbackConstants)
        }
    }
}