package cn.super12138.todo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 带动画且比 Material 3 内置组件动画好看的的可扩展 FAB
 * * （内置组件的动画总是卡一下。。。）
 *
 * 将缩放部分转为最简单的`AnimatedVisibility`实现
 * @param text FAB 的文本
 * @param icon FAB 的前置图标
 * @param expanded 是否为展开状态
 * @param onClick 点击 FAB 后的回调
 * @param modifier `Modifier` 修改器
 */
@Composable
fun AnimatedExtendedFloatingActionButton(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    expanded: Boolean,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        elevation = elevation,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            // Spacer(Modifier.width(if (expanded) 8.dp else 0.dp))
            AnimatedVisibility(expanded) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    text()
                }
            }
        }
    }
}