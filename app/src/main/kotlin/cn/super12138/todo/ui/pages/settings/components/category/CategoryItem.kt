package cn.super12138.todo.ui.pages.settings.components.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    name: String,
    onClick: (String) -> Unit = {},
    onDelete: (String) -> Unit = {}
) {
    val view = LocalView.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = {
                    VibrationUtils.performHapticFeedback(view)
                    onClick(name)
                }
            )
            .padding(
                horizontal = TodoDefaults.settingsItemHorizontalPadding,
                vertical = TodoDefaults.settingsItemVerticalPadding / 2
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
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
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.action_delete)
            )
        }
    }
}