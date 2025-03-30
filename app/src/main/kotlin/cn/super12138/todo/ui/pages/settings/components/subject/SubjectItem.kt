package cn.super12138.todo.ui.pages.settings.components.subject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun SubjectItem(
    modifier: Modifier = Modifier,
    name: String,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val view = LocalView.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.large)
            .clickable {
                VibrationUtils.performHapticFeedback(view)
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                VibrationUtils.performHapticFeedback(view)
                onDelete()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }
    }
}