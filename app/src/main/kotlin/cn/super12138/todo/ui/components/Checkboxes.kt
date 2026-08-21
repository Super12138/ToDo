package cn.super12138.todo.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun CheckboxWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .toggleable(
                value = checked,
                onValueChange = {
                    VibrationUtils.performHapticFeedback(view, HapticFeedbackConstants.LONG_PRESS)
                    onCheckedChange(it)
                },
                role = Role.Checkbox,
                indication = null,
                interactionSource = interactionSource,
            )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        Checkbox(
            checked = checked,
            onCheckedChange = {
                VibrationUtils.performHapticFeedback(view, HapticFeedbackConstants.LONG_PRESS)
                onCheckedChange(it)
            },
            interactionSource = interactionSource
        )
    }
}