package cn.super12138.todo.ui.pages.tasks.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import cn.super12138.todo.R
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.ui.theme.fadeScale
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun TasksTopAppBar(
    inSearchMode: Boolean,
    inSelectionMode: Boolean,
    selectedTasksIds: Set<Int>,
    onEnterSearchMode: () -> Unit,
    onCancelSelect: () -> Unit,
    onSelectAll: () -> Unit,
    onDeleteSelectedTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navIconEnterTransition = fadeIn(
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    ) + expandIn(
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        expandFrom = Alignment.CenterStart
    )

    val navIconExitTransition = fadeOut(
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    ) + shrinkOut(
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        shrinkTowards = Alignment.CenterStart
    )

    val actionEnterTransition = fadeIn(
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

    val actionExitTransition = fadeOut(
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
    )

    val defaultTransitionSpec = fadeScale()

    val view = LocalView.current
    val animatedContainerColor by animateColorAsState(
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
        targetValue = if (inSelectionMode) {
            MaterialTheme.colorScheme.surfaceContainerHighest
        } else {
            VerveDoDefaults.Colors.Background
        }
    )

    TopAppBar(
        navigationIcon = {
            AnimatedVisibility(
                visible = inSelectionMode,
                enter = navIconEnterTransition,
                exit = navIconExitTransition
            ) {
                IconButton(
                    shapes = IconButtonDefaults.shapes(),
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
                targetState = inSelectionMode,
                transitionSpec = { defaultTransitionSpec }
            ) {
                if (it) {
                    Text(
                        text = stringResource(
                            R.string.title_selected_count,
                            selectedTasksIds.size
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = stringResource(R.string.page_tasks),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        actions = {
            Row {
                if (inSelectionMode) {
                    ActionMultipleSelection(
                        onSelectAll = onSelectAll,
                        onDeleteSelectedTodo = onDeleteSelectedTodo
                    )
                } else if (!inSearchMode) {
                    IconButton(
                        shapes = IconButtonDefaults.shapes(),
                        onClick = {
                            VibrationUtils.performHapticFeedback(view)
                            onEnterSearchMode()
                        },
                        modifier = modifier
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = stringResource(R.string.action_search)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.Transparent),
        modifier = modifier.drawBehind { drawRect(animatedContainerColor) }
    )
}

@Composable
fun ActionMultipleSelection(
    onSelectAll: () -> Unit,
    onDeleteSelectedTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        IconButton(
            shapes = IconButtonDefaults.shapes(),
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
            shapes = IconButtonDefaults.shapes(),
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
