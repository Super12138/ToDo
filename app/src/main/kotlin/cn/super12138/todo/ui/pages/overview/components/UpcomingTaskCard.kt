package cn.super12138.todo.ui.pages.overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.pages.settings.components.SettingsItem
import cn.super12138.todo.utils.toLocalDateString

@Composable
fun UpcomingTaskCard(
    modifier: Modifier = Modifier,
    nextWeekTodo: List<TodoEntity>,
    containerColor: Color = TodoDefaults.ContainerColor
) {
    Card(
        modifier = modifier.height(300.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = TodoDefaults.defaultShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TodoDefaults.screenHorizontalPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.title_upcoming_task),
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = nextWeekTodo,
                    key = { it.id }
                ) {
                    SettingsItem(
                        title = it.content,
                        description = it.dueDate.toLocalDateString(),
                        enableClick = false
                    )
                }
            }
        }
    }
}