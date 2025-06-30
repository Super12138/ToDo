package cn.super12138.todo.ui.pages.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Subjects
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton
import cn.super12138.todo.ui.components.ChipItem
import cn.super12138.todo.ui.components.ConfirmDialog
import cn.super12138.todo.ui.components.FilterChipGroup
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.editor.components.TodoContentTextField
import cn.super12138.todo.ui.pages.editor.components.TodoPrioritySlider
import cn.super12138.todo.ui.pages.editor.components.TodoSubjectTextField
import cn.super12138.todo.ui.pages.editor.state.rememberEditorState
import cn.super12138.todo.utils.VibrationUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun TodoEditorPage(
    modifier: Modifier = Modifier,
    toDo: TodoEntity? = null,
    onSave: (TodoEntity) -> Unit,
    onDelete: () -> Unit,
    onNavigateUp: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val view = LocalView.current
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val uiState = rememberEditorState(initialTodo = toDo)
    val isCustomSubject by remember { derivedStateOf { uiState.selectedSubjectId == Subjects.Custom.id } }

    fun checkModifiedBeforeBack() {
        if (uiState.isModified()) {
            uiState.showExitConfirmDialog = true
        } else {
            onNavigateUp()
        }
    }

    BackHandler {
        checkModifiedBeforeBack()
    }

    LargeTopAppBarScaffold(
        title = stringResource(if (toDo != null) R.string.title_edit_task else R.string.action_add_task),
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            with(sharedTransitionScope) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (toDo !== null) {
                        AnimatedExtendedFloatingActionButton(
                            icon = Icons.Outlined.Delete,
                            text = stringResource(R.string.action_delete),
                            expanded = true,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            onClick = { uiState.showDeleteConfirmDialog = true },
                            modifier = Modifier.imePadding()
                        )
                    }
                    AnimatedExtendedFloatingActionButton(
                        icon = Icons.Outlined.Save,
                        text = stringResource(R.string.action_save),
                        expanded = true,
                        onClick = {
                            if (uiState.setErrorIfNotValid()) {
                                return@AnimatedExtendedFloatingActionButton
                            } else {
                                uiState.clearError()
                                onSave(uiState.getEntity())
                            }
                        },
                        modifier = Modifier
                            .imePadding()
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = Constants.KEY_TODO_FAB_TRANSITION),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }
        },
        onBack = { checkModifiedBeforeBack() },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = TodoDefaults.screenPadding)
                .fillMaxSize()
        ) {
            item {
                with(sharedTransitionScope) {
                    TodoContentTextField(
                        value = uiState.toDoContent,
                        onValueChange = { uiState.toDoContent = it },
                        isError = uiState.isErrorContent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("${Constants.KEY_TODO_CONTENT_TRANSITION}_${toDo?.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.label_subject),
                    style = MaterialTheme.typography.titleMedium
                )

                val subjects = remember {
                    Subjects.entries.map {
                        ChipItem(
                            id = it.id,
                            text = it.getDisplayName(context)
                        )
                    }
                }
                FilterChipGroup(
                    items = subjects,
                    defaultSelectedItemIndex = toDo?.subject ?: Subjects.Chinese.id,
                    onSelectedChanged = { uiState.selectedSubjectId = it },
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(
                    visible = isCustomSubject,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    with(sharedTransitionScope) {
                        TodoSubjectTextField(
                            value = uiState.subjectContent,
                            onValueChange = { uiState.subjectContent = it },
                            isError = uiState.isErrorSubject,
                            modifier = Modifier
                                .fillMaxWidth()
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState("${Constants.KEY_TODO_SUBJECT_TRANSITION}_${toDo?.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.label_priority),
                    style = MaterialTheme.typography.titleMedium
                )

                TodoPrioritySlider(
                    value = { uiState.priorityState },
                    onValueChange = { uiState.priorityState = it },
                )
            }

            item {
                if (toDo != null) {
                    Text(
                        text = stringResource(R.string.label_more),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.tip_mark_completed),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Switch(
                            checked = uiState.isCompleted,
                            onCheckedChange = {
                                VibrationUtils.performHapticFeedback(view)
                                uiState.isCompleted = it
                            }
                        )
                    }
                }
            }

            //Spacer(Modifier.size(20.dp))
        }
    }

    ConfirmDialog(
        visible = uiState.showExitConfirmDialog,
        icon = Icons.AutoMirrored.Outlined.Undo,
        text = stringResource(R.string.tip_discard_changes),
        onConfirm = {
            uiState.showExitConfirmDialog = false
            onNavigateUp()
        },
        onDismiss = { uiState.showExitConfirmDialog = false }
    )

    ConfirmDialog(
        visible = uiState.showDeleteConfirmDialog,
        icon = Icons.Outlined.Delete,
        text = stringResource(R.string.tip_delete_task, 1),
        onConfirm = onDelete,
        onDismiss = { uiState.showDeleteConfirmDialog = false }
    )
}