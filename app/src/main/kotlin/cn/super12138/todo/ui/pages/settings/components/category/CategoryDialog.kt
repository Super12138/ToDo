package cn.super12138.todo.ui.pages.settings.components.category

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.BasicDialog

@Composable
fun CategoryPromptDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialCategory: String = "",
    onSave: (oldValue: String, newValue: String) -> Unit,
    onDismiss: () -> Unit
) {
    val textFieldState = rememberTextFieldState(initialText = initialCategory)
    var isError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            textFieldState.setTextAndPlaceCursorAtEnd(initialCategory)
            isError = false
        }
    }

    val supportingText = listOf(
        stringResource(R.string.tip_max_length_5),
        stringResource(R.string.error_no_content_entered),
        stringResource(R.string.error_exceeds_5_chars)
    )

    var currentSupportingText by remember { mutableStateOf(supportingText[0]) }

    BasicDialog(
        visible = visible,
        icon = Icons.Outlined.Info,
        title = stringResource(R.string.tip_tips),
        text = {
            // 已经是实现好滚动的Column布局
            Text(stringResource(R.string.tip_enter_category))
            Spacer(Modifier.size(3.dp))
            OutlinedTextField(
                state = textFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.label_enter_sth)) },
                supportingText = { AnimatedContent(targetState = currentSupportingText) { Text(it) } },
                isError = isError
            )
        },
        confirmButton = stringResource(R.string.action_save),
        dismissButton = stringResource(R.string.action_cancel),
        onConfirm = {
            val trimmedText = textFieldState.text.trim()
            when {
                trimmedText.isEmpty() -> {
                    isError = true
                    currentSupportingText = supportingText[1]
                    return@BasicDialog
                }

                trimmedText.length > 5 -> {
                    isError = true
                    currentSupportingText = supportingText[2]
                    return@BasicDialog
                }

                else -> {
                    onSave(initialCategory, trimmedText.toString())
                    isError = false
                    currentSupportingText = supportingText[0]
                    textFieldState.clearText()
                    onDismiss()
                }
            }
        },
        onDismiss = onDismiss,
        properties = DialogProperties(),
        modifier = modifier
    )
}