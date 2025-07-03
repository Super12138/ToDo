package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.logic.datastore.DataStoreManager
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton
import cn.super12138.todo.ui.components.FiveCharPromptDialog
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.SettingsCategory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDataCategory(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var showDialog by rememberSaveable { mutableStateOf(false) }
    val categories by DataStoreManager.categoriesFlow.collectAsState(initial = emptyList())

    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_category_category_management),
        onBack = onNavigateUp,
        scrollBehavior = scrollBehavior,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AnimatedExtendedFloatingActionButton(
                icon = Icons.Outlined.Add,
                text = stringResource(R.string.action_add_category),
                expanded = isExpanded,
                onClick = { showDialog = true }
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (categories.isEmpty()) {
                item {
                    Text("空")
                }
            } else {
                items(items = categories, key = { it }) {
                    SettingsCategory(it)
                }
            }
        }
    }

    FiveCharPromptDialog(
        visible = showDialog,
        text = stringResource(R.string.tip_enter_category),
        onSave = {
            if (!categories.contains(it)) {
                scope.launch {
                    DataStoreManager.setCategories(categories + it)
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.error_category_duplicate)
                    )
                }
            }
        },
        onDismiss = { showDialog = false }
    )
}