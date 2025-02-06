package cn.super12138.todo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R

@Composable
fun WarningDialog(
    visible: Boolean,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    description: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicDialog(
        visible = visible,
        icon = icon,
        title = stringResource(R.string.title_warning),
        text = { Text(description) },
        confirmButton = stringResource(R.string.action_confirm),
        dismissButton = stringResource(R.string.action_cancel),
        onConfirm = {
            onConfirm()
            onDismiss()
        },
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
fun BasicDialog(
    visible: Boolean,
    icon: ImageVector,
    title: String,
    text: @Composable (() -> Unit)? = null,
    confirmButton: String,
    dismissButton: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicDialog(
        visible = visible,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null // 会跟下面的文本重复，所以设置为 null
            )
        },
        title = { Text(title) },
        text = text,
        confirmButton = {
            FilledTonalButton(onClick = onConfirm) {
                Text(confirmButton)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButton)
            }
        },
        onDismissRequest = onDismiss,
        modifier = modifier
    )
}

@Composable
fun BasicDialog(
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    text: @Composable (() -> Unit)? = null,
    confirmButton: (@Composable () -> Unit),
    dismissButton: (@Composable () -> Unit)? = null,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (visible) {
        AlertDialog(
            icon = icon,
            title = title,
            text = text,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            onDismissRequest = onDismissRequest,
            modifier = modifier
        )
    }
}