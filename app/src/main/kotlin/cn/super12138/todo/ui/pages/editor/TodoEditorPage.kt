package cn.super12138.todo.ui.pages.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var showExitConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val view = LocalView.current
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var toDoContent by rememberSaveable { mutableStateOf(toDo?.content ?: "") }
    var isErrorContent by rememberSaveable { mutableStateOf(false) }
    var selectedSubjectId by rememberSaveable { mutableIntStateOf(toDo?.subject ?: 0) }
    var subjectContent by rememberSaveable { mutableStateOf(toDo?.customSubject ?: "") }
    var isErrorSubject by rememberSaveable { mutableStateOf(false) }
    var priorityState by rememberSaveable { mutableFloatStateOf(toDo?.priority ?: 0f) }
    var completedSwitchState by rememberSaveable { mutableStateOf(toDo?.isCompleted == true) }

    val isCustomSubject by remember {
        derivedStateOf { selectedSubjectId == Subjects.Custom.id }
    }

    fun checkModifiedBeforeBack() {
        var isModified = false
        if ((toDo?.content ?: "") != toDoContent) isModified = true
        if ((toDo?.subject ?: 0) != selectedSubjectId) isModified = true
        if ((toDo?.customSubject ?: "") != subjectContent) isModified = true
        if ((toDo?.priority ?: 0f) != priorityState) isModified = true
        if ((toDo?.isCompleted == true) != completedSwitchState) isModified = true
        if (isModified) {
            showExitConfirmDialog = true
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
                            onClick = { showDeleteConfirmDialog = true },
                            modifier = Modifier.imePadding()
                        )
                    }
                    AnimatedExtendedFloatingActionButton(
                        icon = Icons.Outlined.Save,
                        text = stringResource(R.string.action_save),
                        expanded = true,
                        onClick = {
                            isErrorContent = toDoContent.trim().isEmpty()
                            isErrorSubject = subjectContent.trim()
                                .isEmpty() && selectedSubjectId == Subjects.Custom.id
                            if (isErrorContent || isErrorSubject) return@AnimatedExtendedFloatingActionButton

                            isErrorContent = false
                            isErrorSubject = false
                            onSave(
                                TodoEntity(
                                    content = toDoContent,
                                    subject = selectedSubjectId,
                                    customSubject = subjectContent,
                                    isCompleted = completedSwitchState,
                                    priority = priorityState,
                                    id = toDo?.id ?: 0
                                )
                            )
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = TodoDefaults.screenPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            with(sharedTransitionScope) {
                TodoContentTextField(
                    value = toDoContent,
                    onValueChange = { toDoContent = it },
                    isError = isErrorContent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("${Constants.KEY_TODO_CONTENT_TRANSITION}_${toDo?.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }

            Spacer(Modifier.size(5.dp))

            Text(
                text = stringResource(R.string.label_subject),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.size(5.dp))

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
                onSelectedChanged = {
                    selectedSubjectId = it
                },
                modifier = Modifier.fillMaxWidth()
            )
            AnimatedVisibility(isCustomSubject) {
                with(sharedTransitionScope) {
                    TextField(
                        value = subjectContent,
                        onValueChange = { subjectContent = it },
                        label = { Text(stringResource(R.string.label_enter_subject_name)) },
                        isError = isErrorSubject,
                        supportingText = {
                            AnimatedVisibility(isErrorSubject) {
                                Text(stringResource(R.string.error_no_content_entered))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("${Constants.KEY_TODO_SUBJECT_TRANSITION}_${toDo?.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .padding(top = 5.dp)
                    )
                }
            }

            Spacer(Modifier.size(10.dp))

            Text(
                text = stringResource(R.string.label_priority),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.size(5.dp))

            TodoPrioritySlider(
                value = priorityState,
                onValueChange = { priorityState = it },
            )

            Spacer(Modifier.size(10.dp))

            if (toDo != null) {
                Text(
                    text = stringResource(R.string.label_more),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.size(5.dp))

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
                        checked = completedSwitchState,
                        onCheckedChange = {
                            completedSwitchState = it
                            VibrationUtils.performHapticFeedback(view)
                        }
                    )
                }
            }

            Spacer(Modifier.size(20.dp))
        }
    }

    ConfirmDialog(
        visible = showExitConfirmDialog,
        icon = Icons.AutoMirrored.Outlined.Undo,
        text = stringResource(R.string.tip_discard_changes),
        onConfirm = {
            showExitConfirmDialog = false
            onNavigateUp()
        },
        onDismiss = { showExitConfirmDialog = false }
    )

    ConfirmDialog(
        visible = showDeleteConfirmDialog,
        icon = Icons.Outlined.Delete,
        text = stringResource(R.string.tip_delete_task, 1),
        onConfirm = onDelete,
        onDismiss = { showDeleteConfirmDialog = false }
    )
}