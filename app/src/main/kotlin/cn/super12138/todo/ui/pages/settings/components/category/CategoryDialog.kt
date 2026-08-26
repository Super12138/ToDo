package cn.super12138.todo.ui.pages.settings.components.category

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.DialogProperties
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.BasicDialog

@Composable
fun CategoryPromptDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialCategory: String = "",
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val supportingText = stringResource(R.string.tip_short_category)
    val errorText = stringResource(R.string.error_no_content_entered)

    val textFieldState = rememberTextFieldState(initialText = initialCategory)
    var validate by remember { mutableStateOf(false) }
    val isError by remember { derivedStateOf { validate && textFieldState.text.trim().isEmpty() } }

    fun dismiss() {
        onDismiss()
        textFieldState.clearText()
        validate = false
    }

    fun save(category: String) {
        validate = true
        if (isError) return

        onSave(category)
        dismiss()
    }

    SideEffect(visible) {
        if (visible) {
            textFieldState.setTextAndPlaceCursorAtEnd(initialCategory)
        }
    }

    BasicDialog(
        visible = visible,
        painter = painterResource(R.drawable.ic_info),
        title = stringResource(R.string.tip_tips),
        text = {
            // 已经是实现好滚动的Column布局
            OutlinedTextField(
                state = textFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text(stringResource(R.string.tip_enter_category)) },
                supportingText = { AnimatedContent(targetState = isError) { Text(if (it) errorText else supportingText) } },
                isError = isError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onKeyboardAction = { save(textFieldState.text.trim().toString()) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = stringResource(R.string.action_save),
        dismissButton = stringResource(R.string.action_cancel),
        onConfirm = { save(textFieldState.text.trim().toString()) },
        onDismiss = { dismiss() },
        properties = DialogProperties(),
        modifier = modifier
    )
}