package cn.super12138.todo.ui.pages.main.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopAppBar(
    selectedTodoIds: List<Int>,
    selectedMode: Boolean,
    onCancelSelect: () -> Unit,
    onSelectAll: () -> Unit,
    onDeleteSelectedTodo: () -> Unit,
    toSettingsPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedTopAppBarColors by animateColorAsState(
        targetValue = if (selectedMode) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surface
    )

    TopAppBar(
        navigationIcon = {
            AnimatedVisibility(selectedMode) {
                IconButton(onClick = onCancelSelect) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.tip_clear_selected_items)
                    )
                }
            }
        },
        title = {
            Text(
                text = if (!selectedMode) {
                    stringResource(R.string.app_name)
                } else {
                    stringResource(
                        R.string.title_selected_count,
                        selectedTodoIds.size
                    )
                }
            )
        },
        actions = {
            AnimatedContent(selectedMode) { inSelectedMode ->
                if (!inSelectedMode) {
                    IconButton(
                        onClick = toSettingsPage
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.page_settings)
                        )
                    }
                } else {
                    Row {
                        IconButton(onClick = onSelectAll) {
                            Icon(
                                imageVector = Icons.Outlined.SelectAll,
                                contentDescription = stringResource(R.string.tip_select_all)
                            )
                        }
                        IconButton(onClick = onDeleteSelectedTodo) {
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
}