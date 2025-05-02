package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import cn.super12138.todo.ui.TodoDefaults

@Composable
fun RowSettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    title: String,
    description: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    scrollState: ScrollState = rememberScrollState(),
    fadedEdgeWidth: Dp,
    maskColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable RowScope.() -> Unit
) {
    MoreContentSettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = trailingContent,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // 渲染到离屏缓冲区是为了确保边缘淡出的 alpha 效果仅应用于文本本身，而不影响该可组合项下方绘制的内容（例如窗口背景）。
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    // 需要调用 drawContent，因为它用于将内容绘制到布局中的接收器作用域，允许内容穿插在其他画布操作之间绘制。
                    // 如果未调用 drawContent，则不会绘制该布局的内容。
                    drawContent()
                    drawFadedEdge(
                        edgeWidth = fadedEdgeWidth,
                        maskColor = maskColor,
                        leftEdge = true
                    )
                    drawFadedEdge(
                        edgeWidth = fadedEdgeWidth,
                        maskColor = maskColor,
                        leftEdge = false
                    )
                }
                .horizontalScroll(scrollState),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}

@Composable
fun MoreContentSettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    title: String,
    description: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape)
            .padding(
                horizontal = TodoDefaults.settingsItemHorizontalPadding,
                vertical = TodoDefaults.settingsItemVerticalPadding
            )
    ) {
        Row {
            leadingIcon?.let {
                it()
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                )
                description?.let {
                    Text(
                        text = it,
                        // maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            trailingContent?.let {
                it()
            }
        }

        content()
    }
}

fun ContentDrawScope.drawFadedEdge(
    edgeWidth: Dp,
    maskColor: Color,
    leftEdge: Boolean
) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush =
            Brush.horizontalGradient(
                colors = listOf(Color.Transparent, maskColor),
                startX = if (leftEdge) 0f else size.width,
                endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
            ),
        blendMode = BlendMode.DstIn
    )
}