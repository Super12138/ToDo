package cn.super12138.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import cn.super12138.todo.R
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun FiveCharPromptDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: ImageVector = Icons.Outlined.Info,
    title: String = stringResource(R.string.tip_tips),
    text: String,
    confirmButtonText: String = stringResource(R.string.action_save),
    showDismissButton: Boolean = true,
    dismissButtonText: String = stringResource(R.string.action_cancel),
    properties: DialogProperties = DialogProperties(),
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val textFieldState = rememberTextFieldState()
    var isError by rememberSaveable { mutableStateOf(false) }

    val supportingText = listOf(
        stringResource(R.string.tip_max_length_5),
        stringResource(R.string.error_no_content_entered),
        stringResource(R.string.error_exceeds_5_chars)
    )

    var currentSupportingText by remember { mutableStateOf(supportingText[0]) }

    BasicDialog(
        visible = visible,
        icon = icon,
        title = title,
        text = { // 已经是实现好滚动的Column布局
            Text(text)
            OutlinedTextField(
                state = textFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.label_enter_sth)) },
                supportingText = { Text(currentSupportingText) },
                isError = isError
            )
        },
        confirmButton = confirmButtonText,
        dismissButton = if (showDismissButton) dismissButtonText else null,
        onConfirm = {
            if (textFieldState.text.trim().isEmpty()) {
                isError = true
                currentSupportingText = supportingText[1]
                return@BasicDialog
            } else if (textFieldState.text.trim().length > 5) {
                isError = true
                currentSupportingText = supportingText[2]
            } else {
                onSave(textFieldState.text.toString())
                onDismiss()
            }
        },
        onDismiss = onDismiss,
        properties = properties,
        modifier = modifier
    )
}

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
                }
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
                    }
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