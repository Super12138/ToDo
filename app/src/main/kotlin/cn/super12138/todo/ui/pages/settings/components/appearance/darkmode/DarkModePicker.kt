package cn.super12138.todo.ui.pages.settings.components.appearance.darkmode

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.pages.settings.components.LazyRowSettingsItem
import cn.super12138.todo.ui.theme.DarkMode

@Composable
fun DarkModePicker(
    currentDarkMode: DarkMode,
    onDarkModeChange: (darkMode: DarkMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isInDarkTheme = isSystemInDarkTheme()
    LazyRowSettingsItem(
        title = stringResource(R.string.pref_dark_mode),
        description = stringResource(R.string.pref_dark_mode_desc),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        fadedEdgeWidth = 8.dp,
        modifier = modifier
    ) {
        item {
            DarkModeItem(
                icon = DarkMode.FollowSystem.icon,
                name = DarkMode.FollowSystem.getDisplayName(context),
                contentColor = if (isInDarkTheme) Color.White else Color.Black,
                containerColor = if (isInDarkTheme) Color.Black else Color.White,
                selected = currentDarkMode == DarkMode.FollowSystem,
                onSelect = { onDarkModeChange(DarkMode.FollowSystem) }
            )
        }

        item {
            DarkModeItem(
                icon = DarkMode.Light.icon,
                name = DarkMode.Light.getDisplayName(context),
                contentColor = Color.Black,
                containerColor = Color.White,
                selected = currentDarkMode == DarkMode.Light,
                onSelect = { onDarkModeChange(DarkMode.Light) }
            )
        }

        item {
            DarkModeItem(
                icon = DarkMode.Dark.icon,
                name = DarkMode.Dark.getDisplayName(context),
                contentColor = Color.White,
                containerColor = Color.Black,
                selected = currentDarkMode == DarkMode.Dark,
                onSelect = { onDarkModeChange(DarkMode.Dark) }
            )
        }
    }
}