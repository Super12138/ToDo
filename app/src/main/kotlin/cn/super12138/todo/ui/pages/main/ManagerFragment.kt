package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ManagerFragment(
    modifier: Modifier = Modifier,
    state: LazyListState,
    list: List<TodoEntity>,
    onItemClick: (TodoEntity) -> Unit = {},
    onItemLongClick: (TodoEntity) -> Unit = {},
    onItemChecked: (TodoEntity) -> Unit = {},
    selectedTodoIds: List<Int>
) {
    LazyColumnCustomScrollBar(
        state = state,
        modifier = modifier
    ) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(
                start = TodoDefaults.screenHorizontalPadding,
                bottom = TodoDefaults.toDoCardHeight / 2,
                end = TodoDefaults.screenHorizontalPadding
            ),
            /*modifier = Modifier
            .clip(
                MaterialTheme.shapes.extraLarge.copy(
                    bottomEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(0.dp)
                )
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)*/
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
                        selected = selectedTodoIds.contains(item.id),
                        onCardClick = { onItemClick(item) },
                        onCardLongClick = { onItemLongClick(item) },
                        onChecked = { onItemChecked(item) },
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .animateItem(
                                fadeInSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
                                placementSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
                                fadeOutSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                            )
                    )
                }
            }
        }
    }
}