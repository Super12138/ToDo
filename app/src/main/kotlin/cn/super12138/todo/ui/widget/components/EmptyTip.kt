package cn.super12138.todo.ui.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import cn.super12138.todo.R
import cn.super12138.todo.utils.GlanceTypography

@Composable
fun GlanceTaskEmptyTip(modifier: GlanceModifier = GlanceModifier) {
    val context = LocalContext.current
    val emptyTip = remember(context) { context.getString(R.string.tip_no_task_brief) }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emptyTip,
            style = GlanceTypography.labelLarge.copy(color = GlanceTheme.colors.onSurfaceVariant)
        )
    }
}