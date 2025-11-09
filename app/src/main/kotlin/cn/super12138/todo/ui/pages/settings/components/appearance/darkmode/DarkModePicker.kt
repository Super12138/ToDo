package cn.super12138.todo.ui.pages.settings.components.appearance.darkmode

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    currentDarkMode: () -> DarkMode,
    onDarkModeChange: (darkMode: DarkMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isInDarkTheme = isSystemInDarkTheme()

    val darkModeList = remember { DarkMode.entries.toList() }

    LazyRowSettingsItem(
        title = stringResource(R.string.pref_dark_mode),
        description = stringResource(R.string.pref_dark_mode_desc),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        fadedEdgeWidth = 8.dp,
        modifier = modifier
    ) {
        items(items = darkModeList, key = { it.id }) {
            val (contentColor, containerColor) = when (it) {
                DarkMode.FollowSystem -> if (isInDarkTheme) Color.White to Color.Black else Color.Black to Color.White
                DarkMode.Light -> Color.Black to Color.White
                DarkMode.Dark -> Color.White to Color.Black
            }

            val isSelected by remember { derivedStateOf { currentDarkMode() == it } }

            DarkModeItem(
                icon = it.icon,
                name = it.getDisplayName(context),
                contentColor = contentColor,
                containerColor = containerColor,
                selected = isSelected,
                onSelect = { onDarkModeChange(it) })
        }
    }
}