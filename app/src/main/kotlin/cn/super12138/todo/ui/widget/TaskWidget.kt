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
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
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
import cn.super12138.todo.utils.containerColor
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
                    modifier = GlanceModifier
                        .padding(VerveDoDefaults.contentPadding)
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
    if (taskList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .appWidgetBackground()
                .widgetCornerRadius(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "全部任务都完成啦", style = Typography.titleLarge)
        }
    } else {
        Scaffold(
            titleBar = {
                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Vertical.CenterVertically,
                ) {
                    Text(
                        text = "${taskList.size} 项任务未完成",
                        style = Typography.titleLarge,
                        maxLines = 1,
                        modifier = GlanceModifier.defaultWeight(),
                    )
                }
            },
            // Scaffold内部包含fillMaxSize Modifier
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

    val priorityText = remember(priority) { context.getString(priority.nameRes) }
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
                style = Typography.titleMedium,
                maxLines = 1
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier.fillMaxWidth()
            ) {
                Text(
                    text = category,
                    style = Typography.labelMedium.copy(
                        color = GlanceTheme.colors.onSurfaceVariant
                    ),
                    maxLines = 1
                )
                Spacer(GlanceModifier.size(VerveDoDefaults.contentPadding))
                Text(
                    text = priorityText,
                    style = Typography.labelMedium.copy(
                        color = priority.containerColor().toColorProvider()
                    ),
                    maxLines = 1
                )
            }
            dueDateMillis?.let {
                val dueDate = remember(dueDateMillis) { dueDateMillis.toLocalDateString() }
                Text(
                    text = dueDate,
                    style = Typography.labelMedium.copy(
                        color = GlanceTheme.colors.onSurfaceVariant
                    ),
                    maxLines = 1,
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }
        }

        CircleIconButton(
            imageProvider = ImageProvider(R.drawable.ic_check),
            contentDescription = checkButtonDescription,
            onClick = action(block = onChecked),
            backgroundColor = VerveDoDefaults.Colors.Green.toColorProvider(),
            contentColor = Color.White.toColorProvider()
        )
    }
}

private data class FixedColorProvider(val color: Color) : ColorProvider {
    override fun getColor(context: Context): Color = color
}

private fun Color.toColorProvider() = FixedColorProvider(this)

private fun GlanceModifier.widgetCornerRadius(): GlanceModifier {
    val cornerRadiusModifier =
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            GlanceModifier.cornerRadius(android.R.dimen.system_app_widget_background_radius)
        } else {
            GlanceModifier
        }

    return this.then(cornerRadiusModifier)
}

private object Typography {
    val defaultColor: ColorProvider
        @Composable get() = GlanceTheme.colors.onSurface
    val titleLarge: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        )
    val titleMedium: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    val labelMedium: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
}
