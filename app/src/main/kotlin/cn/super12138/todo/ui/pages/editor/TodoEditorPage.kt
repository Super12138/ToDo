package cn.super12138.todo.ui.pages.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.logic.model.Subjects
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton
import cn.super12138.todo.ui.components.FilterChipGroup
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.components.WarningDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun TodoEditorPage(
    toDo: TodoEntity? = null,
    onSave: (TodoEntity) -> Unit,
    onDelete: () -> Unit,
    onNavigateUp: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    var showExitConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var toDoContent by rememberSaveable { mutableStateOf(toDo?.content ?: "") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var selectedSubjectIndex by rememberSaveable { mutableIntStateOf(toDo?.subject ?: 0) }
    var priorityState by rememberSaveable { mutableFloatStateOf(toDo?.priority ?: 0f) }

    fun checkModifiedBeforeBack() {
        var isModified = false
        if ((toDo?.content ?: "") != toDoContent) isModified = true
        if ((toDo?.subject ?: 0) != selectedSubjectIndex) isModified = true
        if ((toDo?.priority ?: 0f) != priorityState) isModified = true
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
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            },
                            text = { Text(stringResource(R.string.action_delete)) },
                            expanded = true,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            onClick = { showDeleteConfirmDialog = true }
                        )
                    }
                    AnimatedExtendedFloatingActionButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Save,
                                contentDescription = null
                            )
                        },
                        text = { Text(stringResource(R.string.action_save)) },
                        expanded = true,
                        onClick = {
                            if (toDoContent.trim().isEmpty()) {
                                isError = true
                                return@AnimatedExtendedFloatingActionButton
                            }

                            isError = false
                            onSave(
                                TodoEntity(
                                    content = toDoContent,
                                    subject = selectedSubjectIndex,
                                    isCompleted = toDo?.isCompleted ?: false,
                                    priority = priorityState,
                                    id = toDo?.id ?: 0
                                )
                            )
                        },
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = Constants.KEY_TODO_FAB_TRANSITION),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        },
        onBack = {
            checkModifiedBeforeBack()
        },
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
                TextField(
                    value = toDoContent,
                    onValueChange = { toDoContent = it },
                    label = { Text(stringResource(R.string.placeholder_add_todo)) },
                    isError = isError,
                    supportingText = {
                        AnimatedVisibility(isError) {
                            Text(stringResource(R.string.error_no_task_content))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("${Constants.KEY_TODO_CONTENT_TRANSITION}_${toDo?.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }

            Spacer(Modifier.size(5.dp))

            val subjects = remember {
                Subjects.entries.map {
                    it.getDisplayName(context)
                }
            }
            Text(
                text = stringResource(R.string.label_subject),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.size(5.dp))

            FilterChipGroup(
                items = subjects,
                defaultSelectedItemIndex = toDo?.subject ?: 0,
                onSelectedChanged = {
                    selectedSubjectIndex = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(10.dp))

            Text(
                text = stringResource(R.string.label_priority),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.size(5.dp))

            val interactionSource = remember { MutableInteractionSource() }

            Slider(
                modifier = Modifier.semantics {
                    contentDescription = context.getString(R.string.label_priority)
                },
                value = priorityState,
                onValueChange = { priorityState = it },
                valueRange = -10f..10f,
                steps = 3,
                interactionSource = interactionSource,
                thumb = {
                    Label(
                        label = {
                            PlainTooltip(
                                modifier = Modifier
                                    .sizeIn(45.dp, 25.dp)
                                    .wrapContentWidth()
                            ) {
                                Text(Priority.fromFloat(priorityState).getDisplayName(context))
                            }
                        },
                        interactionSource = interactionSource
                    ) {
                        SliderDefaults.Thumb(interactionSource)
                    }
                }
            )

            Spacer(Modifier.size(40.dp))
        }
    }

    WarningDialog(
        visible = showExitConfirmDialog,
        icon = Icons.AutoMirrored.Outlined.Undo,
        description = stringResource(R.string.tip_discard_changes),
        onConfirm = {
            showExitConfirmDialog = false
            onNavigateUp()
        },
        onDismiss = { showExitConfirmDialog = false }
    )

    WarningDialog(
        visible = showDeleteConfirmDialog,
        icon = Icons.Outlined.Delete,
        description = stringResource(R.string.tip_delete_task, 1),
        onConfirm = onDelete,
        onDismiss = { showDeleteConfirmDialog = false }
    )
}