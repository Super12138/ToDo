package cn.super12138.todo.ui.pages.settings

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import cn.super12138.todo.R
import cn.super12138.todo.logic.datastore.DataStoreManager
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.category.CategoryItem
import cn.super12138.todo.ui.pages.settings.components.category.CategoryPromptDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDataCategory(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: 本页及其相关组件重组性能检查优化
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var initialCategory by rememberSaveable { mutableStateOf("") }
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
                onClick = {
                    initialCategory = ""
                    showDialog = true
                }
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
                    Text(
                        text = stringResource(R.string.tip_no_category_page),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                items(items = categories, key = { it }) {
                    CategoryItem(
                        name = it,
                        onClick = { category ->
                            initialCategory = category
                            showDialog = true
                        },
                        onDelete = { category ->
                            scope.launch { DataStoreManager.setCategories(categories - category) }
                        },
                        modifier = Modifier.animateItem(
                            fadeInSpec = tween(100),
                            placementSpec = spring(
                                stiffness = Spring.StiffnessMediumLow,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            ),
                            fadeOutSpec = tween(100)
                        )
                    )
                }
            }
        }

        CategoryPromptDialog(
            visible = showDialog,
            initialCategory = initialCategory,
            onSave = { oldCategory, newCategory ->
                if (oldCategory.isEmpty()) {
                    if (!categories.contains(newCategory)) {
                        scope.launch {
                            DataStoreManager.setCategories(categories + newCategory)
                        }
                    } else {
                        scope.launch {
                            /*snackbarHostState.showSnackbar(
                            message = context.getString(R.string.error_category_duplicate)
                        )*/
                            // 调换分类位置
                            val tempList = categories - newCategory
                            DataStoreManager.setCategories(tempList + newCategory)
                        }
                    }
                } else {
                    if (oldCategory != newCategory) {
                        scope.launch {
                            val tempList = categories - oldCategory
                            DataStoreManager.setCategories(tempList + newCategory)
                        }
                    }
                }
            },
            onDismiss = { showDialog = false }
        )
    }
}