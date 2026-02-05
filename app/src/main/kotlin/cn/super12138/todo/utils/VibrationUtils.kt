package cn.super12138.todo.utils

import android.view.HapticFeedbackConstants
import android.view.View
import cn.super12138.todo.constants.Constants

object VibrationUtils {
    private var isEnabled: Boolean = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT

    /**
     * 启用或禁用触感反馈
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    /**
     * **依据触感反馈的启用状态**为用户提供当前视图的触感反馈
     *
     * 注：触感反馈 **当且仅当 `isHapticFeedbackEnabled()` 为 `true` 时（须在系统设置中开启）** 才会被出发
     *
     * @param view 触发触感反馈的视图
     * @param feedbackConstants 触感反馈类型，默认值为 `HapticFeedbackConstants.CONTEXT_CLICK`
     */
    fun performHapticFeedback(
        view: View,
        feedbackConstants: Int = HapticFeedbackConstants.CONTEXT_CLICK
    ) {
        if (isEnabled) {
            view.performHapticFeedback(feedbackConstants)
        }
    }
}