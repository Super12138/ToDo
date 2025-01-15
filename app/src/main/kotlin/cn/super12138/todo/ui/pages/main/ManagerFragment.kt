package cn.super12138.todo.ui.pages.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Subjects
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.pages.main.components.TodoCard
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun ManagerFragment(
    state: LazyListState,
    list: List<TodoEntity>,
    onItemClick: (TodoEntity) -> Unit = {},
    onItemChecked: (TodoEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LazyColumnScrollbar(
        state = state,
        settings = ScrollbarSettings(
            thumbUnselectedColor = MaterialTheme.colorScheme.secondary,
            thumbSelectedColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
    ) {
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(horizontal = TodoDefaults.screenPadding)
        ) {
            if (list.isEmpty()) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(R.string.tip_no_task),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            } else {
                items(
                    items = list,
                    key = { it.id }
                ) { item ->
                    TodoCard(
                        content = item.content,
                        subject = Subjects.fromId(item.subject).getDisplayName(context),
                        onCardClick = { onItemClick(item) },
                        onChecked = { onItemChecked(item) },
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .animateItem()
                    )
                }
            }
        }
    }
}