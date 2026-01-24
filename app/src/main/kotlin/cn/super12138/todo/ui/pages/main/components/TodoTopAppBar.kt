package cn.super12138.todo.ui.pages.main.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.utils.VibrationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopAppBar(
    selectedTodoIds: List<Int>,
    selectedMode: Boolean,
    onCancelSelect: () -> Unit,
    onSelectAll: () -> Unit,
    onDeleteSelectedTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val animatedTopAppBarColors by animateColorAsState(
        targetValue = if (selectedMode) {
            MaterialTheme.colorScheme.surfaceContainerHighest
        } else {
            TodoDefaults.BackgroundColor
        }
    )

    TopAppBar(
        navigationIcon = {
            AnimatedVisibility(
                visible = selectedMode,
                enter = fadeIn() + expandIn(expandFrom = Alignment.CenterStart),
                exit = shrinkOut(shrinkTowards = Alignment.CenterStart) + fadeOut()
            ) {
                IconButton(
                    onClick = {
                        VibrationUtils.performHapticFeedback(view)
                        onCancelSelect()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.tip_clear_selected_items)
                    )
                }
            }
        },
        title = {
            AnimatedContent(
                targetState = !selectedMode,
                transitionSpec = {
                    (
                            fadeIn(animationSpec = tween(100)) +
                                    scaleIn(initialScale = 0.92f)
                            ).togetherWith(
                            fadeOut(animationSpec = tween(100))
                        )
                }
            ) {
                if (it) {
                    Text(
                        text = stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.title_selected_count,
                            selectedTodoIds.size
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(visible = selectedMode) {
                Row {
                    IconButton(
                        onClick = {
                            VibrationUtils.performHapticFeedback(view)
                            onSelectAll()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_select_all),
                            contentDescription = stringResource(R.string.tip_select_all)
                        )
                    }
                    IconButton(
                        onClick = {
                            VibrationUtils.performHapticFeedback(view)
                            onDeleteSelectedTodo()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.action_delete)
                        )
                    }
                }
            }

        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = animatedTopAppBarColors
        ),
        modifier = modifier
    )
}

@Preview(locale = "zh-rCN", showBackground = true)
@Composable
private fun TodoTopAppBarPreview() {
    var selectedMode by remember { mutableStateOf(false) }
    TodoTopAppBar(
        selectedTodoIds = (1..10).toList(),
        selectedMode = selectedMode,
        onCancelSelect = { selectedMode = !selectedMode },
        onSelectAll = { },
        onDeleteSelectedTodo = { }
    )
}