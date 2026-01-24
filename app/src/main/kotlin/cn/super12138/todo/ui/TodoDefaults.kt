package cn.super12138.todo.ui

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object TodoDefaults {
    /**
     * 屏幕左右两边预留边距（防止内容全部贴边显示过丑）
     */
    val screenHorizontalPadding = 16.dp

    /**
     * 屏幕上下预留边距（防止内容全部贴边显示过丑）
     */
    val screenVerticalPadding = 16.dp

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
    val settingsItemVerticalPadding = 16.dp

    val settingsItemPadding = 2.dp

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

    val ScreenContainerShape: Shape
        @Composable
        get() = MaterialTheme.shapes.large/*.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        )*/

    val ContainerColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.surfaceBright

    val BackgroundColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.surfaceContainer

    val SettingsItemDefaultShape: CornerBasedShape
        @Composable
        get() = MaterialTheme.shapes.small

    val SettingsItemRoundedShape: CornerBasedShape
        @Composable
        get() = MaterialTheme.shapes.large

    val fadedEdgeWidth = 8.dp
}