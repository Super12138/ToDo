package cn.super12138.todo.ui.pages.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

    val selectedEditTodoItem = viewModel.selectedEditTodoItem
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.page_settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            TodoFAB(
                expanded = isExpanded,
                onClick = {
                    openBottomSheet = true
                }
            )
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
                        openBottomSheet = true
                        viewModel.setEditTodoItem(item)
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
                        openBottomSheet = true
                        viewModel.setEditTodoItem(item)
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
                viewModel.setEditTodoItem(null)
                viewModel.addTodo(toDo)
                scope
                    .launch { bottomSheetState.hide() }
                    .invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet = false
                        }
                    }
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
            }
        )
    }
}