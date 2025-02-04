package cn.super12138.todo.ui.pages.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.model.Priority

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoCard(
    content: String,
    subject: String,
    completed: Boolean,
    priority: Float,
    selected: Boolean,
    onCardClick: () -> Unit = {},
    onCardLongClick: () -> Unit = {},
    onChecked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onCardClick,
                    onLongClick = onCardLongClick
                )
                .padding(horizontal = 15.dp)
        ) {
            AnimatedVisibility(selected) {
                Box(
                    Modifier
                        .padding(end = 15.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSecondaryContainer)
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        contentDescription = stringResource(R.string.tip_select_this)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = when (priority) {
                                -10f -> MaterialTheme.colorScheme.surfaceContainerHighest
                                -5f -> MaterialTheme.colorScheme.surfaceContainerHighest
                                0f -> MaterialTheme.colorScheme.secondary
                                5f -> MaterialTheme.colorScheme.tertiary
                                10f -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.secondary
                            },
                            modifier = Modifier.padding(start = 5.dp)
                        ) {
                            Text(Priority.fromFloat(priority).getDisplayName(context))
                        }
                    }
                ) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier.basicMarquee() // TODO: 后续评估性能影响
                    )
                }

                Text(
                    text = subject,
                    style = MaterialTheme.typography.labelMedium,
                    textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1
                )
            }

            AnimatedVisibility(!selected && !completed) {
                IconButton(onClick = onChecked) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(R.string.tip_mark_completed)
                    )
                }
            }

            /*Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(50.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .clickable {
                        onChecked()
                    }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = ""
                )
            }*/
        }
    }
}

@Preview(locale = "zh-rCN", showBackground = true)
@Composable
private fun TodoCardPreview() {
    TodoCard(
        content = "背《岳阳楼记》《出师表》《琵琶行》",
        subject = "语文",
        completed = false,
        priority = Priority.Important.value,
        selected = false,
        onCardClick = {},
        onCardLongClick = {},
        onChecked = {}
    )
}