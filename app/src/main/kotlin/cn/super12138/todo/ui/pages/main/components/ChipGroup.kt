package cn.super12138.todo.ui.pages.main.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R

/**
 * 部分参考：https://github.com/Rhythamtech/FilterChipGroup-Compose-Android/blob/main/FilterChipGroup.kt
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChipGroup(
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    onSelectedChanged: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(defaultSelectedItemIndex) }

    FlowRow(modifier = modifier) {
        items.forEachIndexed { index, item ->
            FilterChip(
                selected = items[selectedItemIndex] == items[index],
                onClick = {
                    selectedItemIndex = index
                    onSelectedChanged(index)
                },
                leadingIcon = {
                    AnimatedVisibility(
                        visible = items[selectedItemIndex] == items[index]
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.tips_select_this),
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                },
                label = {
                    Text(item)
                },
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}