package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R

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
            modifier = Modifier.size(175.dp)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}