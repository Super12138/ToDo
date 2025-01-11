package cn.super12138.todo.ui.pages.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.pages.main.components.TodoCard

@Composable
fun ManagerFragment(
    state: LazyListState,
    list: List<TodoEntity>,
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
        items(
            items = list,
            key = { it.id }
        ) { item ->
            TodoCard(
                content = item.content,
                subject = item.subject,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .animateItem()
            )
        }
    }
}