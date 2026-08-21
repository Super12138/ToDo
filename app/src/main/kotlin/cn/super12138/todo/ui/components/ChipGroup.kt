package cn.super12138.todo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun FilterChipGroup(
    items: List<ChipItem>,
    modifier: Modifier = Modifier,
    selectedItemId: Int? = null,
    onSelectedChanged: (ChipItem) -> Unit = {}
) {
    val view = LocalView.current
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(VerveDoDefaults.contentPadding)
    ) {
        items.forEach {
            with(it) {
                val selected = selectedItemId == id
                FilterChip(
                    selected = selected,
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = stringResource(R.string.tip_selected),
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null,
                    label = { Text(text = label, maxLines = 1) },
                    onClick = {
                        onSelectedChanged(this)
                        VibrationUtils.performHapticFeedback(view)
                    },
                    shapes = FilterChipDefaults.shapes()
                )
            }
        }
    }
}

data class ChipItem(
    val id: Int,
    val label: String
)