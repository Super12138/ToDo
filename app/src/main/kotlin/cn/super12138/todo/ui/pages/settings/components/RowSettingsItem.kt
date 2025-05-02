package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import cn.super12138.todo.ui.TodoDefaults

@Composable
fun RowSettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    title: String,
    description: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    scrollState: ScrollState = rememberScrollState(),
    rowModifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    MoreContentSettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = trailingContent,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .then(rowModifier),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}

@Composable
fun MoreContentSettingsItem(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    title: String,
    description: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(shape)
            .padding(
                horizontal = TodoDefaults.settingsItemHorizontalPadding,
                vertical = TodoDefaults.settingsItemVerticalPadding
            )
    ) {
        Row {
            leadingIcon?.let {
                it()
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                )
                description?.let {
                    Text(
                        text = it,
                        // maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            trailingContent?.let {
                it()
            }
        }

        content()
    }
}