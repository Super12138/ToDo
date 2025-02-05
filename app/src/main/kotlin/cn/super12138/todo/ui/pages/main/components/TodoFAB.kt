package cn.super12138.todo.ui.pages.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton

/**
 * 待办页面底部 FAB
 *
 * @param expanded 是否为展开状态（展开状态显示内容和图标，收起状态只显示图标）
 * @param onClick 当点击 FAB 时的回调
 */
@Composable
fun TodoFAB(
    expanded: Boolean,
    onClick: () -> Unit,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    modifier: Modifier = Modifier
) {
    AnimatedExtendedFloatingActionButton(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.action_add_task)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.action_add_task),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        },
        expanded = expanded,
        elevation = elevation,
        onClick = onClick,
        modifier = modifier
    )
}