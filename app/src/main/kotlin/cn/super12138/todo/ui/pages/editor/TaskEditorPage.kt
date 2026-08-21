package cn.super12138.todo.ui.pages.editor

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.ui.components.CheckboxWithLabel
import cn.super12138.todo.ui.components.ChipItem
import cn.super12138.todo.ui.components.ConfirmDialog
import cn.super12138.todo.ui.components.FilterChipGroup
import cn.super12138.todo.ui.components.TodoFloatingActionButton
import cn.super12138.todo.ui.components.TopAppBarScaffold
import cn.super12138.todo.ui.components.bottomPadding
import cn.super12138.todo.ui.pages.editor.components.DueDateChooser
import cn.super12138.todo.utils.VibrationUtils
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SharedTransitionScope.TaskAddPage(
    modifier: Modifier = Modifier,
    onSave: (TaskEntity) -> Unit,
    onNavigateUp: () -> Unit
) = TaskEditorPage(
    task = null,
    modifier = modifier
        .sharedBounds(
            sharedContentState = rememberSharedContentState(key = Constants.KEY_TODO_FAB_TRANSITION),
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
        )
        .skipToLookaheadSize(), // 这个修饰符必须放后面
    onSave = onSave,
    onDelete = {},
    onNavigateUp = onNavigateUp
)

@Composable
fun SharedTransitionScope.TaskEditPage(
    modifier: Modifier = Modifier,
    task: TaskEntity,
    onSave: (TaskEntity) -> Unit,
    onDelete: () -> Unit,
    onNavigateUp: () -> Unit
) = TaskEditorPage(
    task = task,
    modifier = modifier
        .sharedBounds(
            sharedContentState = rememberSharedContentState(key = "${Constants.KEY_TODO_ITEM_TRANSITION}_${task.id}"),
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
        )
        .skipToLookaheadSize(),
    onSave = onSave,
    onDelete = onDelete,
    onNavigateUp = onNavigateUp
)

@Composable
fun TaskEditorPage(
    modifier: Modifier = Modifier,
    task: TaskEntity? = null,
    onNavigateUp: () -> Unit,
    onSave: (TaskEntity) -> Unit,
    onDelete: () -> Unit,
    viewModel: EditorViewModel = koinViewModel { parametersOf(task) }
) {
    val view = LocalView.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val isContentError by remember { derivedStateOf { uiState.content.isEmpty() } }
    val isCategoryError by remember { derivedStateOf { uiState.category.isEmpty() } }

    fun navigateUpIfUnchanged() {
        if (viewModel.isModified()) {
            viewModel.showExitConfirmDialog()
        } else {
            onNavigateUp()
        }
    }

    SideEffect(uiState.shouldAutoFocusContent) { focusRequester.requestFocus() }

    BackHandler(onBack = ::navigateUpIfUnchanged)

    TopAppBarScaffold(
        title = stringResource(if (task == null) R.string.action_add_task else R.string.title_edit_task),
        navigationIcon = {
            FilledIconButton(
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                shapes = IconButtonDefaults.shapes(),
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    navigateUpIfUnchanged()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.action_back)
                )
            }
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(VerveDoDefaults.contentPadding),
                modifier = Modifier.imePadding()
            ) {
                if (task != null) {
                    TodoFloatingActionButton(
                        text = stringResource(R.string.action_delete),
                        iconRes = R.drawable.ic_delete,
                        expanded = true,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        onClick = viewModel::showDeleteConfirmDialog
                    )
                }
                TodoFloatingActionButton(
                    text = stringResource(R.string.action_save),
                    iconRes = R.drawable.ic_save,
                    expanded = true,
                    onClick = {
                        if (isContentError || isCategoryError) return@TodoFloatingActionButton
                        onSave(viewModel.getNewTaskEntity())
                    }
                )
            }
        },
        modifier = modifier
    ) {
        /*val contentField = rememberTextFieldState()
        val categoryField = rememberTextFieldState()*/
        val customizationText = stringResource(R.string.label_customization)

        val categoryChipList = remember(uiState.categoryList) {
            uiState.categoryList.mapIndexed { index, category ->
                ChipItem(index, category)
            } + ChipItem(-1, customizationText)
        }

        /*LaunchedEffect(contentField, categoryField) {
            snapshotFlow { contentField.text.trim().toString() }
                .collect { }

            snapshotFlow { categoryField.text.trim().toString() }
                .collect { }
        }*/

        SideEffect(categoryChipList) { //看看要不要改成uiState.categoryList
            val id = if (task == null) {
                if (categoryChipList.size == 1) -1 else 0
            } else {
                task.category findIdIn categoryChipList
            }
            viewModel.setSelectedCategory(categoryChipList.firstOrNull { it.id == id })
            Log.d(Constants.TAG, "TaskEditorPage: SetSelectedId Successfully")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(VerveDoDefaults.contentPadding * 2),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Subtitle(R.string.placeholder_add_todo)
                /*TextField(
                    state = contentField,
                    label = { Text(stringResource(R.string.placeholder_add_todo)) },
                    lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 3),
                    isError = isContentError,
                    supportingText = {
                        AnimatedVisibility(
                            visible = isContentError,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Text(
                                text = stringResource(R.string.error_no_content_entered),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )*/
                TextField(
                    value = uiState.content,
                    onValueChange = { viewModel.setContentText(it) },
                    label = { Text(stringResource(R.string.placeholder_add_todo)) },
                    maxLines = 3,
                    isError = isContentError,
                    supportingText = {
                        AnimatedVisibility(
                            visible = isContentError,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Text(
                                text = stringResource(R.string.error_no_content_entered),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }
            item {
                Subtitle(R.string.label_category)
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (uiState.categoryList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.tip_no_category_chip),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    FilterChipGroup(
                        items = categoryChipList,
                        selectedItemId = uiState.selectedCategoryId,
                        onSelectedChanged = { viewModel.setSelectedCategory(it) }
                    )
                    AnimatedVisibility(uiState.selectedCategoryId == -1) {
                        /*TextField(
                            state = categoryField,
                            label = { Text(stringResource(R.string.label_enter_category_name)) },
                            isError = isCategoryError,
                            supportingText = {
                                AnimatedContent(
                                    targetState = isCategoryError,
                                    // transitionSpec = { enterTransition togetherWith exitTransition }
                                ) { error ->
                                    Text(
                                        text = if (error) {
                                            stringResource(R.string.error_no_content_entered)
                                        } else {
                                            stringResource(R.string.tip_short_category)
                                        },
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            },
                            lineLimits = TextFieldLineLimits.SingleLine
                        )*/
                        TextField(
                            value = uiState.category,
                            onValueChange = { viewModel.setCategoryText(it) },
                            label = { Text(stringResource(R.string.label_enter_category_name)) },
                            isError = isCategoryError,
                            supportingText = {
                                AnimatedContent(
                                    targetState = isCategoryError,
                                    // transitionSpec = { enterTransition togetherWith exitTransition }
                                ) { error ->
                                    Text(
                                        text = if (error) {
                                            stringResource(R.string.error_no_content_entered)
                                        } else {
                                            stringResource(R.string.tip_short_category)
                                        },
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            item {
                val priorityList = Priority.entries.reversed()

                Subtitle(R.string.label_priority)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                    verticalArrangement = Arrangement.spacedBy(VerveDoDefaults.contentPadding / 4),
                ) {
                    priorityList.forEachIndexed { index, priority ->
                        ToggleButton(
                            content = { Text(stringResource(priority.nameRes)) },
                            checked = uiState.priority == priority,
                            onCheckedChange = {
                                viewModel.setPriority(priority)
                                VibrationUtils.performHapticFeedback(view)
                            },
                            shapes = index.toggleButtonShapesIn(priorityList),
                            colors = VerveDoDefaults.toggleButtonColors,
                            modifier = Modifier.semantics { role = Role.RadioButton }
                        )
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.label_more),
                    style = MaterialTheme.typography.titleMedium
                )
                DueDateChooser(
                    dateMillis = uiState.dueDateMillis,
                    onDateChange = { viewModel.setDueDate(it) }
                )
                if (task != null) {
                    CheckboxWithLabel(
                        label = stringResource(R.string.tip_mark_completed),
                        checked = uiState.isCompleted,
                        onCheckedChange = { viewModel.setCompleted(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            bottomPadding()
        }
    }

    ConfirmDialog(
        visible = uiState.showExitConfirmDialog,
        iconRes = R.drawable.ic_undo,
        text = stringResource(R.string.tip_discard_changes),
        onConfirm = {
            viewModel.hideExitConfirmDialog()
            onNavigateUp()
        },
        onDismiss = { viewModel.hideExitConfirmDialog() }
    )

    ConfirmDialog(
        visible = uiState.showDeleteConfirmDialog,
        iconRes = R.drawable.ic_delete,
        text = stringResource(R.string.tip_delete_task, 1),
        onConfirm = onDelete,
        onDismiss = { viewModel.hideDeleteConfirmDialog() }
    )
}

@Composable
private fun LazyItemScope.Subtitle(@StringRes titleRes: Int) =
    Text(
        text = stringResource(titleRes),
        style = MaterialTheme.typography.titleMedium
    )

private infix fun String.findIdIn(chipList: List<ChipItem>) =
    chipList.firstOrNull { item -> item.label == this }?.id ?: -1

@Composable
private fun Int.toggleButtonShapesIn(list: List<Priority>) = when (this) {
    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
    list.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
}
