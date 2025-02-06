package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cn.super12138.todo.ui.pages.settings.state.rememberPrefBooleanState

@Composable
fun SwitchSettingsItem(
    key: String,
    default: Boolean,
    leadingIcon: ImageVector? = null,
    title: String,
    description: String? = null,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var switchState by rememberPrefBooleanState(key, default)
    SettingsItem(
        leadingIcon = leadingIcon,
        title = title,
        description = description,
        trailingContent = {
            Switch(
                checked = switchState,
                onCheckedChange = {
                    switchState = it
                    onCheckedChange(it)
                },
                modifier = Modifier.padding(start = 14.dp)
            )
        },
        onClick = {
            switchState = !switchState
            onCheckedChange(switchState)
        },
        modifier = modifier
    )
}