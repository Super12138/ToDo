package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cn.super12138.todo.ui.TodoDefaults

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Settings(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .padding(vertical = TodoDefaults.screenVerticalPadding)
            .clip(MaterialTheme.shapes.extraLarge),
        content = content
    )
}