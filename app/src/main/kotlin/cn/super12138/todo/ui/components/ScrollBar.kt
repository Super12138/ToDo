package cn.super12138.todo.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun LazyColumnCustomScrollBar(
    state: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    LazyColumnScrollbar(
        state = state,
        modifier = modifier,
        settings = ScrollbarSettings(
            thumbUnselectedColor = MaterialTheme.colorScheme.secondary,
            thumbSelectedColor = MaterialTheme.colorScheme.primary
        ),
        content = content
    )
}