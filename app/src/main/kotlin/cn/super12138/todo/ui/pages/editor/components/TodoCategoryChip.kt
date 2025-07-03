package cn.super12138.todo.ui.pages.editor.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.super12138.todo.ui.components.ChipItem
import cn.super12138.todo.ui.components.FilterChipGroup

@Composable
fun TodoCategoryChip(
    modifier: Modifier = Modifier,
    items: List<ChipItem>,
    defaultSelectedItemIndex: Int,
    isLoading: Boolean = false,
    onCategorySelected: (Int) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = { fadeIn(tween(100)) togetherWith fadeOut(tween(100)) }
        ) {
            if (it) {
                CircularProgressIndicator()
            } else {
                FilterChipGroup(
                    modifier = Modifier,
                    items = items,
                    defaultSelectedItemIndex = defaultSelectedItemIndex,
                    onSelectedChanged = onCategorySelected
                )
            }
        }
    }
}