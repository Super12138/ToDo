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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.pages.main.components.TodoCard

@Composable
fun ManagerFragment(
    state: LazyListState,
    list: List<TodoEntity>,
    onItemClick: (TodoEntity) -> Unit = {},
    onItemChecked: (TodoEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 10.dp,
        )
    ) {
        if (list.isEmpty()) {
            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.tips_no_task),
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
                    subject = item.subject,
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