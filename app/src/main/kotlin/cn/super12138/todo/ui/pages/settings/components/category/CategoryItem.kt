package cn.super12138.todo.ui.pages.settings.components.category

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    name: String,
    onDelete: (String) -> Unit = {}
) {
    val view = LocalView.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.large)
            .padding(
                horizontal = TodoDefaults.settingsItemHorizontalPadding,
                vertical = TodoDefaults.settingsItemVerticalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            ),
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                VibrationUtils.performHapticFeedback(view)
                onDelete(name)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.action_delete)
            )
        }
    }
}