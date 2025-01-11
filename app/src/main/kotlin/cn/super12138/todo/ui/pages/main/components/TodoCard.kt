package cn.super12138.todo.ui.pages.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodoCard(
    content: String,
    subject: String,
    onCardClick: () -> Unit = {},
    onChecked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        onClick = onCardClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = subject,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            IconButton(onClick = onChecked) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = ""
                )
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