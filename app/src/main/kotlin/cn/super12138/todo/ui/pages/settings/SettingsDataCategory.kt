package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import cn.super12138.todo.R
import cn.super12138.todo.logic.datastore.DataStoreManager
import cn.super12138.todo.ui.components.TodoFloatingActionButton
import cn.super12138.todo.ui.components.TopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.SettingsContainer
import cn.super12138.todo.ui.pages.settings.components.SettingsItem
import cn.super12138.todo.ui.pages.settings.components.category.CategoryPromptDialog
import cn.super12138.todo.utils.VibrationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsDataCategory(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: 本页及其相关组件重组性能检查优化
    val view = LocalView.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var initialCategory by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val categories by DataStoreManager.categoriesFlow.collectAsState(initial = emptyList())

    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    // TODO: 取消5字分类限制
    TopAppBarScaffold(
        title = stringResource(R.string.pref_category_category_management),
        onBack = onNavigateUp,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            TodoFloatingActionButton(
                iconRes = R.drawable.ic_add,
                text = stringResource(R.string.action_add_category),
                expanded = isExpanded,
                onClick = {
                    initialCategory = ""
                    showDialog = true
                }
            )
        },
        modifier = modifier,
    ) {
        SettingsContainer(Modifier.fillMaxSize()) {
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
                // Keep stable content key (category) for animations, but compute rounding based on content
                items(
                    items = categories,
                    key = { it }
                ) { category ->
                    // compute rounding by content rather than by index so insertion/moves
                    // correctly update rounded corners while keeping content key for animations
                    val topRounded = category == categories.first()
                    val bottomRounded = category == categories.last()

                    SettingsItem(
                        headlineContent = {
                            Text(
                                text = category,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.basicMarquee()
                            )
                        },
                        trailingContent = {
                            FilledTonalIconButton(
                                shapes = IconButtonDefaults.shapes(),
                                onClick = {
                                    VibrationUtils.performHapticFeedback(view)
                                    scope.launch { DataStoreManager.setCategories(categories - category) }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_delete),
                                    contentDescription = stringResource(R.string.action_delete)
                                )
                            }
                        },
                        onClick = {
                            initialCategory = category
                            showDialog = true
                        },
                        topRounded = topRounded,
                        bottomRounded = bottomRounded,
                        modifier = Modifier.animateItem(
                            fadeInSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
                            placementSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
                            fadeOutSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
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