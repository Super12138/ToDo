package cn.super12138.todo.ui.pages.editor.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R

@Composable
fun TodoContentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.placeholder_add_todo)) },
        isError = isError,
        supportingText = {
            AnimatedVisibility(isError) {
                Text(stringResource(R.string.error_no_content_entered))
            }
        },
        modifier = modifier
    )
}

@Composable
fun TodoCategoryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.label_enter_category_name)) },
        isError = isError,
        supportingText = {
            AnimatedVisibility(isError) {
                Text(stringResource(R.string.error_no_content_entered))
            }
        },
        modifier = modifier
    )
}