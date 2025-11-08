package cn.super12138.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cn.super12138.todo.R
import cn.super12138.todo.utils.VibrationUtils
import com.kyant.capsule.ContinuousRoundedRectangle

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: ImageVector = Icons.Outlined.ErrorOutline,
    title: String = stringResource(R.string.title_warning),
    text: String,
    confirmButtonText: String = stringResource(R.string.action_confirm),
    showDismissButton: Boolean = true,
    dismissButtonText: String = stringResource(R.string.action_cancel),
    properties: DialogProperties = DialogProperties(),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicDialog(
        visible = visible,
        icon = icon,
        title = title,
        text = { Text(text) }, // 已经实现好滚动了
        confirmButton = confirmButtonText,
        dismissButton = if (showDismissButton) dismissButtonText else null,
        onConfirm = {
            onConfirm()
            onDismiss()
        },
        onDismiss = onDismiss,
        properties = properties,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BasicDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: ImageVector,
    title: String,
    text: @Composable (() -> Unit)? = null,
    confirmButton: String,
    dismissButton: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties()
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
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                text?.let { it() }
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onConfirm()
                },
                shapes = ButtonDefaults.shapes(
                    /*shape = ContinuousRoundedRectangle(50.dp),
                    pressedShape = ContinuousRoundedRectangle(12.dp)*/
                )
            ) {
                Text(confirmButton)
            }
        },
        dismissButton = {
            dismissButton?.let {
                TextButton(
                    onClick = {
                        VibrationUtils.performHapticFeedback(view)
                        onDismiss()
                    },
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(it)
                }
            }
        },
        onDismissRequest = onDismiss,
        properties = properties,
        modifier = modifier
    )
}

@Composable
fun BasicDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    text: @Composable (() -> Unit)? = null,
    confirmButton: (@Composable () -> Unit),
    dismissButton: (@Composable () -> Unit)? = null,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties()
) {
    if (visible) {
        AlertDialog(
            icon = icon,
            title = title,
            text = text,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            onDismissRequest = onDismissRequest,
            properties = properties,
            modifier = modifier
        )
    }
}