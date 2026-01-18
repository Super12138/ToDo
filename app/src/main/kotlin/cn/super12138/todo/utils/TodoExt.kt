package cn.super12138.todo.utils

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import cn.super12138.todo.logic.model.Priority

@Composable
@Stable
fun Priority.containerColor(): Color =
    when (this) {
        Priority.NotUrgent -> MaterialTheme.colorScheme.surfaceContainerHighest
        Priority.NotImportant -> MaterialTheme.colorScheme.surfaceContainerHighest
        Priority.Default -> MaterialTheme.colorScheme.secondary
        Priority.Important -> MaterialTheme.colorScheme.tertiary
        Priority.Urgent -> MaterialTheme.colorScheme.error
    }

@Composable
fun CornerBasedShape.getPartialRoundedShape(
    topRounded: Boolean,
    bottomRounded: Boolean,
    roundedShape: CornerBasedShape
): CornerBasedShape =
    this.copy(
        topStart = if (topRounded) roundedShape.topStart else this.topStart,
        topEnd = if (topRounded) roundedShape.topEnd else this.topEnd,
        bottomEnd = if (bottomRounded) roundedShape.bottomEnd else this.bottomEnd,
        bottomStart = if (bottomRounded) roundedShape.bottomStart else this.bottomStart,
    )


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