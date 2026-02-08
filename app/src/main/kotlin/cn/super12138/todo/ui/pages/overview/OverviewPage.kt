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
import java.util.Calendar

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OverviewPage(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val today = Calendar.getInstance().apply {
        // 将时间设置为当天的开始（00:00:00.000）
        // 兼容API24
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val toDos by viewModel.sortedTodos.collectAsState(initial = emptyList())
    val totalTasks by remember { derivedStateOf { toDos.size } }
    val completedTasks by remember { derivedStateOf { toDos.count { it.isCompleted } } }
    val nextWeekTodo by remember {
        derivedStateOf {
            val now = System.currentTimeMillis()
            val weekFromNow = now + 7L * 24 * 60 * 60 * 1000
            toDos.filter { todo ->
                val due = todo.dueDate ?: return@filter false
                due >= today.timeInMillis && due < weekFromNow
            }
        }
    }


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