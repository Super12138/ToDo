package cn.super12138.todo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.utils.VibrationUtils

/**
 * 部分参考：https://github.com/Rhythamtech/FilterChipGroup-Compose-Android/blob/main/FilterChipGroup.kt
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChipGroup(
    items: List<ChipItem>,
    defaultSelectedItemIndex: Int = 0,
    onSelectedChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var selectedItemId by rememberSaveable { mutableIntStateOf(defaultSelectedItemIndex) }

    FlowRow(modifier = modifier) {
        items.forEach { item ->
            FilterChipItem(
                selected = item.id == selectedItemId,
                text = item.text,
                onClick = {
                    selectedItemId = item.id
                    VibrationUtils.performHapticFeedback(view)
                    onSelectedChanged(item.id)
                }
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            AnimatedVisibility(
                visible = selected,
                enter = expandIn(expandFrom = Alignment.CenterStart) + fadeIn(),
                exit = shrinkOut(shrinkTowards = Alignment.CenterStart) + fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = stringResource(R.string.tip_select_this),
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        },
        label = {
            Text(text)
        },
        modifier = Modifier.padding(end = 10.dp)
    )
}

data class ChipItem(
    val id: Int,
    val text: String
)