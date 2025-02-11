package cn.super12138.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import cn.super12138.todo.R
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun WarningDialog(
    visible: Boolean,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    description: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(),
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
        properties = properties,
        modifier = modifier
    )
}

@Composable
fun ConfirmDialog(
    visible: Boolean,
    icon: ImageVector,
    title: String,
    text: String,
    confirmButton: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    BasicDialog(
        visible = visible,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null // 会跟下面的文本重复，所以设置为 null
            )
        },
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(confirmButton)
            }
        },
        dismissButton = {},
        onDismissRequest = onDismiss,
        properties = properties,
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
    properties: DialogProperties = DialogProperties(),
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
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
            FilledTonalButton(
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onConfirm()
                }
            ) {
                Text(confirmButton)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onDismiss()
                }
            ) {
                Text(dismissButton)
            }
        },
        onDismissRequest = onDismiss,
        properties = properties,
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
    properties: DialogProperties = DialogProperties(),
    modifier: Modifier = Modifier
) {
    if (visible) {
        AlertDialog(
            icon = icon,
            title = title,
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    text?.let { it() }
                }
            },
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            onDismissRequest = onDismissRequest,
            properties = properties,
            modifier = modifier
        )
    }
}