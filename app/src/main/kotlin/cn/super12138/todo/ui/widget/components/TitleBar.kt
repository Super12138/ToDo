package cn.super12138.todo.ui.widget.components

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import cn.super12138.todo.utils.GlanceTypography

@Composable
fun GlanceTitleBar(
    title: String,
    taskCount: String,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
        Text(
            text = title,
            style = GlanceTypography.titleLarge,
            maxLines = 1,
            modifier = GlanceModifier.defaultWeight()
        )

        Text(
            text = taskCount,
            style = GlanceTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 1
        )
    }
}