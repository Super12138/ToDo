package cn.super12138.todo.ui.pages.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.pages.main.components.TodoDeleteConfirmDialog
import cn.super12138.todo.ui.pages.main.components.TodoFAB
import cn.super12138.todo.ui.pages.main.components.TodoTopAppBar
import cn.super12138.todo.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    viewModel: MainViewModel,
    toSettingsPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toDoList = viewModel.toDos.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val isExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val selectedEditTodoItem = viewModel.selectedEditTodo
    val selectedTodoIds = viewModel.selectedTodoIds.collectAsState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isSelectedIdsEmpty by remember {
        derivedStateOf {
            selectedTodoIds.value.isEmpty()
        }
    }

    val showCompleted = true // 后续替换成用户设置
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
            AnimatedVisibility(
                visible = isSelectedIdsEmpty,
                enter = fadeIn() + expandIn(),
                exit = shrinkOut() + fadeOut()
            ) {
                TodoFAB(
                    expanded = isExpanded,
                    onClick = { showBottomSheet = true }
                )
            }
        },
        contentWindowInsets = WindowInsets.safeContent,
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
                            showBottomSheet = true
                            viewModel.setEditTodoItem(item)
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
                                    id = id
                                )
                            )
                            viewModel.playConfetti()
                        }
                    },
                    selectedTodoIds = selectedTodoIds.value,
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
                            showBottomSheet = true
                            viewModel.setEditTodoItem(item)
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
                                    id = id
                                )
                            )
                            viewModel.playConfetti()
                        }
                    },
                    selectedTodoIds = selectedTodoIds.value,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        }
    }
    if (showBottomSheet) {
        TodoBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                showBottomSheet = false
                viewModel.setEditTodoItem(null)
            },
            toDo = selectedEditTodoItem,
            onSave = { toDo ->
                viewModel.addTodo(toDo)
            },
            onClose = {
                viewModel.setEditTodoItem(null)
                scope
                    .launch { bottomSheetState.hide() }
                    .invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
            },
            onDelete = {
                if (selectedEditTodoItem !== null) viewModel.deleteTodo(selectedEditTodoItem)
            }
        )
    }
    if (showDeleteConfirmDialog) {
        TodoDeleteConfirmDialog(
            onConfirm = {
                showDeleteConfirmDialog = false
                viewModel.apply {
                    deleteSelectedTodo()
                }
            },
            onDismiss = { showDeleteConfirmDialog = false },
            deleteTodoCount = selectedTodoIds.value.size
        )
    }
}