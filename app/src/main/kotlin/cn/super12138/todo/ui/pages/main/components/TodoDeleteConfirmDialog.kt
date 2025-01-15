package cn.super12138.todo.ui.pages.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R

@Composable
fun TodoDeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    deleteTodoCount: Int,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.action_delete)
            )
        },
        title = {
            Text(stringResource(R.string.title_warning))
        },
        text = {
            Text(stringResource(R.string.tip_delete_task, deleteTodoCount))
        },
        confirmButton = {
            FilledTonalButton(onClick = onConfirm) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        onDismissRequest = onDismiss
    )
}