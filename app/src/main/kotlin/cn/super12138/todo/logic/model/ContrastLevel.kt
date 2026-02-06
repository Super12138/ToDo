package cn.super12138.todo.logic.model

import androidx.annotation.StringRes
import cn.super12138.todo.R

enum class ContrastLevel(
    val value: Float,
    @param:StringRes val nameRes: Int
) {
    VeryLow(value = -1f, nameRes = R.string.contrast_very_low),
    Low(value = -0.5f, nameRes = R.string.contrast_low),
    Default(value = 0f, nameRes = R.string.contrast_default),
    Medium(value = 0.5f, nameRes = R.string.contrast_high),
    High(value = 1f, nameRes = R.string.contrast_very_high);

    companion object {
        fun fromFloat(float: Float) = entries.find { it.value == float } ?: Default
    }
}