package cn.super12138.todo.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.action
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import cn.super12138.todo.R
import cn.super12138.todo.logic.TaskRepository
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.utils.toLocalDateString
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class TaskWidget : GlanceAppWidget(), KoinComponent {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val taskRepository: TaskRepository = get()

        provideContent {
            val scope = rememberCoroutineScope()
            val allTask by taskRepository.getAllTasks().collectAsState(emptyList())
            val allIncompleteTask = remember(allTask) { allTask.filter { !it.isCompleted } }

            GlanceTheme {
                TaskWidgetApp(
                    taskList = allIncompleteTask,
                    onChecked = { scope.launch { taskRepository.updateTask(it) } },
                    modifier = GlanceModifier.padding(VerveDoDefaults.contentPadding / 2)
                )
            }
        }
    }
}

@Composable
private fun TaskWidgetApp(
    taskList: List<TaskEntity>,
    modifier: GlanceModifier = GlanceModifier,
    onChecked: (TaskEntity) -> Unit = {}
) {
    val incompleteCount = taskList.size

    if (incompleteCount == 0) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .appWidgetBackground()
                .widgetCornerRadius(),
            contentAlignment = Alignment.Center
        ) {
            Text("全部任务都完成啦")
        }
    } else {
        Scaffold(
            titleBar = {
                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Vertical.CenterVertically,
                ) {
                    Text(
                        text = "$incompleteCount 项任务未完成",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        ),
                        maxLines = 1,
                        modifier = GlanceModifier.defaultWeight(),
                    )
                }
            },
            modifier = modifier
        ) {
            LazyColumn {
                items(items = taskList, itemId = { task -> task.id.toLong() }) {
                    TaskCard(
                        content = it.content,
                        category = it.category,
                        dueDateMillis = it.dueDateMillis,
                        priority = Priority.fromFloat(it.priority),
                        onChecked = { onChecked(it.copy(isCompleted = true)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    content: String,
    category: String,
    dueDateMillis: Long?,
    priority: Priority,
    modifier: GlanceModifier = GlanceModifier,
    onChecked: () -> Unit = {}
) {
    val context = LocalContext.current

    val priority = remember(priority) { context.getString(priority.nameRes) }
    val checkButtonDescription =
        remember(context) { context.getString(R.string.tip_mark_completed) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = VerveDoDefaults.settingsItemVerticalPadding / 4)
    ) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.defaultWeight()
        ) {
            Text(
                text = content,
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                ),
                maxLines = 1
            )
            Row {
                Text(text = category)
                Text(text = priority)
            }
            dueDateMillis?.let {
                val dueDate = remember(dueDateMillis) { dueDateMillis.toLocalDateString() }
                Text(dueDate)
            }
        }

        CircleIconButton(
            imageProvider = ImageProvider(R.drawable.ic_check),
            contentDescription = checkButtonDescription,
            onClick = action(block = onChecked),
            backgroundColor = FixedColorProvider(VerveDoDefaults.Colors.Green),
            contentColor = FixedColorProvider(Color.White)
        )
    }
}

data class FixedColorProvider(val color: Color) : ColorProvider {
    override fun getColor(context: Context): Color = color
}

fun GlanceModifier.widgetCornerRadius(): GlanceModifier {
    val cornerRadiusModifier =
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            GlanceModifier.cornerRadius(android.R.dimen.system_app_widget_background_radius)
        } else {
            GlanceModifier
        }

    return this.then(cornerRadiusModifier)
}
