package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SwitchSettingsItem(
    leadingIcon: ImageVector? = null,
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    DefaultSettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}