package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressFragment(
    totalTasks: Int,
    completedTasks: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val thickStrokeWidth = with(LocalDensity.current) { TodoDefaults.trackThickness.toPx() }
    val thickStroke = remember(thickStrokeWidth) {
        Stroke(width = thickStrokeWidth, cap = StrokeCap.Round)
    }

    val remainTasks = totalTasks - completedTasks
    val progress = if (totalTasks != 0) {
        completedTasks / totalTasks.toFloat()
    } else {
        // 没有任务时
        0f
    }

    Box(
        modifier = modifier.padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        CircularWavyProgressIndicator(
            progress = { animatedProgress },
            stroke = thickStroke,
            trackStroke = thickStroke,
            amplitude = { progress ->
                if (progress <= 0.1f || progress >= 0.95f) {
                    0f
                } else {
                    TodoDefaults.waveAmplitude
                }
            },
            wavelength = TodoDefaults.waveLength,
            waveSpeed = TodoDefaults.waveSpeed,
            modifier = Modifier
                .size(175.dp)
                .clearAndSetSemantics {}
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = if (totalTasks != 0) {
                        context.getString(
                            R.string.accessibility_progress_tasks,
                            totalTasks,
                            completedTasks,
                            remainTasks
                        )
                    } else {
                        context.getString(R.string.accessibility_progress_no_tasks)
                    }
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
            AnimatedVisibility(
                visible = remainTasks != 0,
                enter = fadeIn(MaterialTheme.motionScheme.fastSpatialSpec()) + expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()),
                exit = fadeOut(MaterialTheme.motionScheme.fastSpatialSpec()) + shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()),
            ) {
                Text(
                    text = stringResource(R.string.tip_remain_tasks, remainTasks),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clearAndSetSemantics {}
                )
            }
        }
    }
}