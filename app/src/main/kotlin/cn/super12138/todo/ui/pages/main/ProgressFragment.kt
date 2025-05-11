package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.AnimatedCircularProgressIndicator

@Composable
fun ProgressFragment(
    totalTasks: Int,
    completedTasks: Int,
    modifier: Modifier = Modifier
) {
    val remainTasks = totalTasks - completedTasks
    val progress = if (totalTasks != 0) {
        completedTasks / totalTasks.toFloat()
    } else {
        // 没有任务时
        0f
    }

    val progressDescription = if (totalTasks != 0) {
        stringResource(
            R.string.accessibility_progress_tasks,
            totalTasks,
            completedTasks,
            remainTasks
        )
    } else {
        stringResource(R.string.accessibility_progress_no_tasks)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedCircularProgressIndicator(
            progress = progress,
            strokeWidth = 10.dp,
            gapSize = 10.dp,
            modifier = Modifier
                .size(175.dp)
                .clearAndSetSemantics {}
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clearAndSetSemantics {
                    stateDescription = progressDescription
                }
            ) {
                Text(
                    text = completedTasks.toString(),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.placeholder_divider),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = totalTasks.toString(),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            AnimatedVisibility(remainTasks != 0) {
                Text(
                    text = stringResource(R.string.tip_remain_tasks, remainTasks),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clearAndSetSemantics {}
                )
            }
        }
    }
}