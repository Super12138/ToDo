package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun SwitchSettingsItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    leadingIcon: ImageVector? = null,
    title: String,
    description: String? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    val view = LocalView.current
    SettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = {
                    VibrationUtils.performHapticFeedback(view)
                    onCheckedChange(it)
                },
                modifier = Modifier.padding(start = TodoDefaults.settingsItemHorizontalPadding / 2)
            )
        },
        onClick = {
            onCheckedChange(!checked)
        },
        modifier = modifier
    )
}