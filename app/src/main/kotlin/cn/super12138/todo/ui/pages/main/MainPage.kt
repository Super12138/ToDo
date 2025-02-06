package cn.super12138.todo.ui.pages.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowWidthSizeClass
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.components.WarningDialog
import cn.super12138.todo.ui.pages.main.components.TodoFAB
import cn.super12138.todo.ui.pages.main.components.TodoTopAppBar
import cn.super12138.todo.ui.viewmodels.MainViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainPage(
    viewModel: MainViewModel,
    toTodoEditPage: () -> Unit,
    toSettingsPage: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val toDoList = viewModel.sortedTodos.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val isExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val selectedTodoIds = viewModel.selectedTodoIds.collectAsState()
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isSelectedIdsEmpty by remember {
        derivedStateOf {
            selectedTodoIds.value.isEmpty()
        }
    }

    val showCompleted = viewModel.showCompletedTodos
    val filteredTodoList =
        if (showCompleted) toDoList.value else toDoList.value.filter { item -> !item.isCompleted }

    BackHandler(enabled = !isSelectedIdsEmpty) {
        // 当按下返回键（或进行返回操作）时清空选择
        viewModel.clearAllTodoSelection()
    }

    Scaffold(
        topBar = {
            TodoTopAppBar(
                selectedTodoIds = selectedTodoIds.value,
                selectedMode = !isSelectedIdsEmpty,
                onCancelSelect = { viewModel.clearAllTodoSelection() },
                onSelectAll = { viewModel.toggleAllSelected() },
                onDeleteSelectedTodo = { showDeleteConfirmDialog = true },
                toSettingsPage = toSettingsPage
            )
        },
        floatingActionButton = {
            with(sharedTransitionScope) {
                AnimatedVisibility(
                    visible = isSelectedIdsEmpty,
                    enter = fadeIn() + expandIn(),
                    exit = shrinkOut() + fadeOut()
                ) {
                    TodoFAB(
                        expanded = isExpanded,
                        onClick = {
                            viewModel.setEditTodoItem(null) // 每次添加待办前清除上一次已选待办
                            toTodoEditPage()
                        },
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = Constants.KEY_TODO_FAB_TRANSITION),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeContent.exclude(WindowInsets.ime),
        modifier = modifier
    ) { innerPadding ->
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
            Column(modifier = Modifier.padding(innerPadding)) {
                ProgressFragment(
                    totalTasks = toDoList.value.size,
                    completedTasks = toDoList.value.count { it.isCompleted },
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )

                ManagerFragment(
                    state = listState,
                    list = filteredTodoList,
                    onItemClick = { item ->
                        if (isSelectedIdsEmpty) {
                            viewModel.setEditTodoItem(item)
                            toTodoEditPage()
                        } else {
                            viewModel.toggleTodoSelection(item)
                        }
                    },
                    onItemLongClick = { item ->
                        viewModel.toggleTodoSelection(item)
                    },
                    onItemChecked = { item ->
                        item.apply {
                            viewModel.updateTodo(
                                TodoEntity(
                                    content = content,
                                    subject = subject,
                                    isCompleted = true,
                                    priority = priority,
                                    id = id
                                )
                            )
                            viewModel.playConfetti()
                        }
                    },
                    selectedTodoIds = selectedTodoIds.value,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        } else {
            Row(modifier = Modifier.padding(innerPadding)) {
                ProgressFragment(
                    totalTasks = toDoList.value.size,
                    completedTasks = toDoList.value.count { it.isCompleted },
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )
                ManagerFragment(
                    state = listState,
                    list = filteredTodoList,
                    onItemClick = { item ->
                        if (isSelectedIdsEmpty) {
                            viewModel.setEditTodoItem(item)
                            toTodoEditPage()
                        } else {
                            viewModel.toggleTodoSelection(item)
                        }
                    },
                    onItemLongClick = { item ->
                        viewModel.toggleTodoSelection(item)
                    },
                    onItemChecked = { item ->
                        item.apply {
                            viewModel.updateTodo(
                                TodoEntity(
                                    content = content,
                                    subject = subject,
                                    isCompleted = true,
                                    priority = priority,
                                    id = id
                                )
                            )
                            viewModel.playConfetti()
                        }
                    },
                    selectedTodoIds = selectedTodoIds.value,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        }
    }

    WarningDialog(
        visible = showDeleteConfirmDialog,
        icon = Icons.Outlined.Delete,
        description = stringResource(R.string.tip_delete_task, selectedTodoIds.value.size),
        onConfirm = { viewModel.deleteSelectedTodo() },
        onDismiss = { showDeleteConfirmDialog = false }
    )
}