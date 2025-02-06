package cn.super12138.todo.logic.model

import android.content.Context
import cn.super12138.todo.R

enum class ContrastLevel(val value: Float) {
    VeryLow(-1f),
    Low(-0.5f),
    Default(0f),
    Medium(0.5f),
    High(1f);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            VeryLow -> R.string.contrast_very_low
            Low -> R.string.contrast_low
            Default -> R.string.contrast_default
            Medium -> R.string.contrast_high
            High -> R.string.contrast_very_high
        }
        return context.getString(resId)
    }

    companion object {
        fun fromFloat(float: Float): ContrastLevel {
            return ContrastLevel.entries.find { it.value == float } ?: Default
        }
    }
}