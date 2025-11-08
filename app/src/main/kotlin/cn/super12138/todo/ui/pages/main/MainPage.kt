package cn.super12138.todo.ui.pages.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.window.core.layout.WindowSizeClass
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.datastore.DataStoreManager
import cn.super12138.todo.ui.components.ConfirmDialog
import cn.super12138.todo.ui.pages.main.components.TodoTopAppBar
import cn.super12138.todo.ui.viewmodels.MainViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    toTodoEditPage: (TodoEntity?) -> Unit,
    toSettingsPage: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    val toDos = viewModel.sortedTodos.collectAsState(initial = emptyList())
    val selectedTodos = viewModel.selectedTodoIds.collectAsState()
    val showCompleted by DataStoreManager.showCompletedFlow.collectAsState(initial = Constants.PREF_SHOW_COMPLETED_DEFAULT)

    val listState = rememberLazyListState()
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val selectedTodoIds by remember { derivedStateOf { selectedTodos.value } }
    val inSelectedMode by remember { derivedStateOf { !selectedTodoIds.isEmpty() } }
    val toDoList by remember { derivedStateOf { toDos.value } }
    val totalTasks by remember { derivedStateOf { toDoList.size } }
    val completedTasks by remember { derivedStateOf { toDoList.count { it.isCompleted } } }
    val filteredTodoList =
        if (showCompleted) toDoList else toDoList.filter { item -> !item.isCompleted }
    val expandedFab by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    // 当按下返回键（或进行返回操作）时清空选择，仅在非选择模式下生效
    BackHandler(inSelectedMode) { viewModel.clearAllTodoSelection() }

    Scaffold(
        topBar = {
            TodoTopAppBar(
                selectedTodoIds = selectedTodoIds,
                selectedMode = inSelectedMode,
                onCancelSelect = { viewModel.clearAllTodoSelection() },
                onSelectAll = { viewModel.selectAllTodos() },
                onDeleteSelectedTodo = { showDeleteConfirmDialog = true },
                toSettingsPage = toSettingsPage
            )
        },
        floatingActionButton = {
            with(sharedTransitionScope) {
                SmallExtendedFloatingActionButton(
                    text = { Text(stringResource(R.string.action_add_task)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null
                        )
                    },
                    expanded = expandedFab,
                    onClick = { toTodoEditPage(null) },
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = Constants.KEY_TODO_FAB_TRANSITION),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .animateFloatingActionButton(
                            visible = !inSelectedMode,
                            alignment = Alignment.BottomEnd,
                        )
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
    ) { innerPadding ->
        val isMediumScreen =
            windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
        if (isMediumScreen) {
            Row(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                ProgressFragment(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )
                ManagerFragment(
                    state = listState,
                    list = filteredTodoList,
                    onItemClick = { item ->
                        if (inSelectedMode) {
                            viewModel.toggleTodoSelection(item)
                        } else {
                            toTodoEditPage(item)
                        }
                    },
                    onItemLongClick = { viewModel.toggleTodoSelection(it) },
                    onItemChecked = {
                        viewModel.updateTodo(it.copy(isCompleted = true))
                        viewModel.playConfetti()
                    },
                    selectedTodoIds = selectedTodoIds,
                    // sharedTransitionScope = sharedTransitionScope,
                    // animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                ProgressFragment(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                )
                ManagerFragment(
                    state = listState,
                    list = filteredTodoList,
                    onItemClick = { item ->
                        if (inSelectedMode) {
                            viewModel.toggleTodoSelection(item)
                        } else {
                            toTodoEditPage(item)
                        }
                    },
                    onItemLongClick = { viewModel.toggleTodoSelection(it) },
                    onItemChecked = {
                        viewModel.updateTodo(it.copy(isCompleted = true))
                        viewModel.playConfetti()
                    },
                    selectedTodoIds = selectedTodoIds,
                    // sharedTransitionScope = sharedTransitionScope,
                    // animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize()
                )
            }
        }
        ConfirmDialog(
            visible = showDeleteConfirmDialog,
            icon = Icons.Outlined.Delete,
            text = stringResource(R.string.tip_delete_task, selectedTodoIds.size),
            onConfirm = { viewModel.deleteSelectedTodo() },
            onDismiss = { showDeleteConfirmDialog = false }
        )
    }
}