package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Subjects
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.FilterChipGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    toDo: TodoEntity? = null,
    onSave: (TodoEntity) -> Unit,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = TodoDefaults.screenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(if (toDo != null) R.string.title_edit_task else R.string.action_add_task),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.size(20.dp))

            var text by rememberSaveable { mutableStateOf(toDo?.content ?: "") }
            var isError by rememberSaveable { mutableStateOf(false) }
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.placeholder_add_todo)) },
                isError = isError,
                supportingText = {
                    AnimatedVisibility(isError) {
                        Text(stringResource(R.string.error_no_task_content))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(5.dp))

            val subjects = remember {
                Subjects.entries.map {
                    it.getDisplayName(context)
                }
            }
            var selectedItemIndex by rememberSaveable { mutableIntStateOf(toDo?.subject ?: 0) }
            FilterChipGroup(
                items = subjects,
                defaultSelectedItemIndex = toDo?.subject ?: 0,
                onSelectedChanged = {
                    selectedItemIndex = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onClose) {
                    Text(stringResource(R.string.action_cancel))
                }
                if (toDo !== null) {
                    FilledTonalButton(
                        onClick = {
                            onDelete()
                            onClose()
                        },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(stringResource(R.string.action_delete))
                    }
                }
                FilledTonalButton(
                    onClick = {
                        // 文本为空
                        if (text.trim().isEmpty()) {
                            isError = true
                            return@FilledTonalButton
                        }

                        isError = false
                        onSave(
                            TodoEntity(
                                content = text,
                                subject = selectedItemIndex,
                                isCompleted = toDo?.isCompleted ?: false,
                                id = toDo?.id ?: 0
                            )
                        )
                        onClose()
                    }
                ) {
                    Text(stringResource(R.string.action_save))
                }
            }
            Spacer(Modifier.size(30.dp))
        }
    }
}