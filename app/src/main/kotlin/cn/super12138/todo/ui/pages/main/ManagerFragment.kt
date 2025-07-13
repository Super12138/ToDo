package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.LazyColumnCustomScrollBar
import cn.super12138.todo.ui.pages.main.components.TodoCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ManagerFragment(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list: List<TodoEntity>,
    onItemClick: (TodoEntity) -> Unit = {},
    onItemLongClick: (TodoEntity) -> Unit = {},
    onItemChecked: (TodoEntity) -> Unit = {},
    selectedTodoIds: List<Int>,
    // sharedTransitionScope: SharedTransitionScope,
    // animatedVisibilityScope: AnimatedVisibilityScope
) {
    LazyColumnCustomScrollBar(
        state = state,
        modifier = modifier
    ) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(
                start = TodoDefaults.screenPadding,
                bottom = TodoDefaults.toDoCardHeight / 2,
                end = TodoDefaults.screenPadding
            ),
            // verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (list.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.tip_no_task),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(
                    items = list,
                    key = { it.id }
                ) { item ->
                    TodoCard(
                        // id = item.id,
                        content = item.content,
                        category = item.category,
                        completed = item.isCompleted,
                        priority = Priority.fromFloat(item.priority),
                        selected = { selectedTodoIds.contains(item.id) },
                        onCardClick = { onItemClick(item) },
                        onCardLongClick = { onItemLongClick(item) },
                        onChecked = { onItemChecked(item) },
                        // sharedTransitionScope = sharedTransitionScope,
                        // animatedVisibilityScope = animatedVisibilityScope,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .animateItem() // TODO: 设置动画时间
                    )
                }
            }
        }
    }
}