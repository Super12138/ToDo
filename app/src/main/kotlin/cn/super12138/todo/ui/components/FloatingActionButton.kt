package cn.super12138.todo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.super12138.todo.utils.VibrationUtils

/**
 * 带动画且比 Material 3 内置组件动画好看的的可扩展 FAB
 * * （内置组件的动画总是卡一下。。。）
 *
 * 将缩放部分转为最简单的`AnimatedVisibility`实现
 * @param icon FAB 的前置图标
 * @param text FAB 的文本
 * @param textOverflow FAB 文本溢出显示方案
 * @param expanded 是否为展开状态
 * @param containerColor FAB 容器的颜色
 * @param contentColor FAB 文本和图标颜色，通常不需要自己指定，会自动依据容器颜色设置
 * @param elevation FAB 的高度（阴影大小）
 * @param onClick 点击 FAB 后的回调
 * @param modifier `Modifier` 修改器
 */
@Composable
fun AnimatedExtendedFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    textOverflow: TextOverflow = TextOverflow.Clip,
    expanded: Boolean,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    onClick: () -> Unit
) {
    val view = LocalView.current
    FloatingActionButton(
        onClick = {
            VibrationUtils.performHapticFeedback(view)
            onClick()
        },
        elevation = elevation,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
            // Spacer(Modifier.width(if (expanded) 8.dp else 0.dp))
            AnimatedVisibility(expanded) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = text,
                        maxLines = 1,
                        overflow = textOverflow
                    )
                }
            }
        }
    }
}