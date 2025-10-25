package cn.super12138.todo.ui

import androidx.compose.ui.unit.dp

object TodoDefaults {
    /**
     * 屏幕左右两边预留边距（防止内容全部贴边显示过丑）
     */
    val screenPadding = 16.dp

    /**
     * 待办卡片默认高度
     */
    val toDoCardHeight = 80.dp

    /**
     * 设置项水平边距
     */
    val settingsItemHorizontalPadding = 24.dp

    /**
     * 设置项垂直边距
     */
    val settingsItemVerticalPadding = 20.dp

    /**
     * 待办进度条粗度
     */
    val trackThickness = 7.0.dp

    /**
     * 待办进度条波长
     */
    val waveLength = 35.dp

    /**
     * 待办进度条波速
     */
    val waveSpeed = 3.dp

    /**
     * 待办进度条波幅
     */
    const val waveAmplitude = 0.6f
}