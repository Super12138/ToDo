package cn.super12138.todo.ui.widget.today

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyListScope
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import cn.super12138.todo.R
import cn.super12138.todo.logic.TaskRepository
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.logic.model.SortingMethod
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.ui.widget.components.GlanceTaskCard
import cn.super12138.todo.utils.GlanceTypography
import cn.super12138.todo.utils.SystemUtils
import cn.super12138.todo.utils.sort
import cn.super12138.todo.utils.widgetCornerRadius
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class TodayTaskWidget : GlanceAppWidget(), KoinComponent {
    val taskRepository: TaskRepository = get()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val scope = rememberCoroutineScope()
            val allTask by taskRepository.getAllTasks().collectAsState(emptyList())
            val todayTask = remember(allTask) {
                allTask
                    .filter { it.dueDateMillis == SystemUtils.getStartOfDayMillis(0) }
                    .sort(SortingMethod.Priority)
            }

            GlanceTheme {
                TodayTaskApp(
                    taskList = todayTask,
                    onChecked = { scope.launch { taskRepository.updateTask(it) } },
                    modifier = GlanceModifier.padding(VerveDoDefaults.contentPadding)
                )
            }
        }
    }
}

@Composable
private fun TodayTaskApp(
    taskList: List<TaskEntity>,
    modifier: GlanceModifier = GlanceModifier,
    onChecked: (TaskEntity) -> Unit = {}
) {
    val context = LocalContext.current
    val completeTasks = remember(taskList) { taskList.filter { it.isCompleted } }
    val inCompleteTasks = remember(taskList) { taskList.filter { !it.isCompleted } }

    if (taskList.isEmpty()) {
        val allTaskComplete = remember(true) { context.getString(R.string.tip_all_task_complete) }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .appWidgetBackground()
                .widgetCornerRadius(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = allTaskComplete, style = GlanceTypography.titleLarge)
        }
    } else {
        Scaffold(
            titleBar = {
                val title = remember(taskList.size) {
                    context.getString(
                        R.string.label_task_incomplete,
                        taskList.size
                    )
                }
                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Vertical.CenterVertically,
                ) {
                    Text(
                        text = "今日任务",
                        style = GlanceTypography.titleLarge,
                        maxLines = 1,
                        modifier = GlanceModifier.defaultWeight()
                    )

                    Text(
                        text = "${inCompleteTasks.size} 项",
                        style = GlanceTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        modifier = GlanceModifier
                    )
                }
            },
            // Scaffold内部包含fillMaxSize Modifier
            modifier = modifier
        ) {
            LazyColumn {
                if (inCompleteTasks.isNotEmpty()) {
                    title("未完成")
                    items(
                        items = inCompleteTasks,
                        itemId = { task -> task.id.toLong() }
                    ) {
                        GlanceTaskCard(
                            content = it.content,
                            category = it.category,
                            dueDateMillis = it.dueDateMillis,
                            isCompleted = it.isCompleted,
                            showDueDate = false,
                            priority = Priority.fromFloat(it.priority),
                            onChecked = { onChecked(it.copy(isCompleted = true)) }
                        )
                    }
                }

                if (completeTasks.isNotEmpty()) {
                    title("已完成")
                    items(
                        items = completeTasks,
                        itemId = { task -> task.id.toLong() }
                    ) {
                        GlanceTaskCard(
                            content = it.content,
                            category = it.category,
                            dueDateMillis = it.dueDateMillis,
                            isCompleted = it.isCompleted,
                            priority = Priority.fromFloat(it.priority),
                            showDueDate = false,
                            onChecked = { onChecked(it.copy(isCompleted = true)) }
                        )
                    }
                }
            }
        }
    }
}

private fun LazyListScope.title(title: String) {
    item {
        Spacer(GlanceModifier.size(VerveDoDefaults.contentPadding))
    }
    item {
        Text(
            text = title,
            style = GlanceTypography.labelLarge.copy(color = GlanceTheme.colors.primary),
            maxLines = 1
        )
    }
}