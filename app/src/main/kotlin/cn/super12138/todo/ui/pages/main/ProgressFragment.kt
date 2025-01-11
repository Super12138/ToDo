package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.super12138.todo.ui.viewmodels.MainViewModel

@Composable
fun ProgressFragment(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val toDoList = viewModel.toDos.collectAsState(initial = emptyList())
    val totalTask = toDoList.value.size
    val completedTasks = toDoList.value.count { it.isCompleted }
    val progress = if (toDoList.value.isNotEmpty()) {
        completedTasks / totalTask.toFloat()
    } else {
        // 没有任务时
        0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "To Do Progress"
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            strokeWidth = 10.dp,
            gapSize = 10.dp,
            modifier = Modifier.size(170.dp)
        )
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = completedTasks.toString(),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "/",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = totalTask.toString(),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        /*Slider(
            value = progress,
            onValueChange = { progress = it }
        )*/
    }
}