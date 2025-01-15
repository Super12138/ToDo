package cn.super12138.todo.ui.pages.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowWidthSizeClass
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.pages.main.components.TodoFAB
import cn.super12138.todo.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val toDoList = viewModel.toDos.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val isExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val selectedEditTodoItem = viewModel.selectedEditTodo
    val selectedTodoIds = viewModel.selectedTodoIds.collectAsState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isSelectedIdsEmpty by remember {
        derivedStateOf {
            selectedTodoIds.value.isEmpty()
        }
    }

    val animatedTopAppBarColors by animateColorAsState(
        targetValue = if (isSelectedIdsEmpty) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerHighest
    )

    BackHandler(enabled = !isSelectedIdsEmpty) {
        // 当按下返回键（或进行返回操作）时清空选择
        viewModel.clearAllTodoSelection()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    AnimatedVisibility(!isSelectedIdsEmpty) {
                        IconButton(onClick = { viewModel.clearAllTodoSelection() }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.tip_clear_selected_items)
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = if (isSelectedIdsEmpty) {
                            stringResource(R.string.app_name)
                        } else {
                            stringResource(
                                R.string.title_selected_count,
                                selectedTodoIds.value.size
                            )
                        }
                    )
                },
                actions = {
                    AnimatedContent(isSelectedIdsEmpty) { isEmpty ->
                        if (isEmpty) {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = stringResource(R.string.page_settings)
                                )
                            }
                        } else {
                            Row {
                                IconButton(onClick = { viewModel.toggleAllSelected() }) {
                                    Icon(
                                        imageVector = Icons.Outlined.SelectAll,
                                        contentDescription = stringResource(R.string.tip_select_all)
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteSelectedTodo() }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = stringResource(R.string.action_delete)
                                    )
                                }
                            }
                        }
                    }

                },
                colors = TopAppBarDefaults.largeTopAppBarColors().copy(
                    containerColor = animatedTopAppBarColors
                )
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
                    onClick = { openBottomSheet = true }
                )
            }
        },
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
                    list = toDoList.value.filter { item -> !item.isCompleted },
                    onItemClick = { item ->
                        if (isSelectedIdsEmpty) {
                            openBottomSheet = true
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
                    list = toDoList.value.filter { item -> !item.isCompleted },
                    onItemClick = { item ->
                        if (isSelectedIdsEmpty) {
                            openBottomSheet = true
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
    if (openBottomSheet) {
        TodoBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                openBottomSheet = false
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
                            openBottomSheet = false
                        }
                    }
            },
            onDelete = {
                if (selectedEditTodoItem !== null) viewModel.deleteTodo(selectedEditTodoItem)
            }
        )
    }
}