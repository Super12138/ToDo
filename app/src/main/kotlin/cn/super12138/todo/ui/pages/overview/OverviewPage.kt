package cn.super12138.todo.ui.pages.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.TopAppBarScaffold
import cn.super12138.todo.ui.pages.overview.components.RoundedCornerCardLarge
import cn.super12138.todo.ui.pages.overview.components.UpcomingTaskCard
import cn.super12138.todo.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OverviewPage(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val toDos by viewModel.sortedTodos.collectAsState(initial = emptyList())
    val totalTasks by remember { derivedStateOf { toDos.size } }
    val completedTasks by remember { derivedStateOf { toDos.count { it.isCompleted } } }
    val nextWeekTodo by remember { derivedStateOf { toDos.filter { it.dueDate != null && it.dueDate < System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000 } } }

    TopAppBarScaffold(
        title = stringResource(R.string.page_overview),
        modifier = modifier
    ) {
        Column {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp
            ) {
                item {
                    RoundedCornerCardLarge(
                        iconRes = R.drawable.ic_apps,
                        title = "总任务",
                        count = totalTasks
                    )
                }
                item {
                    RoundedCornerCardLarge(
                        iconRes = R.drawable.ic_check_circle,
                        title = "已完成",
                        count = completedTasks,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
                item {
                    RoundedCornerCardLarge(
                        iconRes = R.drawable.ic_pending,
                        title = "未完成",
                        count = totalTasks - completedTasks,
                        containerColor = MaterialTheme.colorScheme.errorContainer // tertiaryContainer
                    )
                }
                item {
                    UpcomingTaskCard(
                        nextWeekTodo = nextWeekTodo
                    )
                }
            }
        }
    }
}