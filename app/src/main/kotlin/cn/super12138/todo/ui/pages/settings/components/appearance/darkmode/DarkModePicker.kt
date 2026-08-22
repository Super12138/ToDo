package cn.super12138.todo.ui.pages.settings.components.appearance.darkmode

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.ui.pages.settings.components.LazyRowSettingsItem

@Composable
fun DarkModePicker(
    currentDarkMode: DarkMode,
    onDarkModeChange: (darkMode: DarkMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val isInDarkTheme = isSystemInDarkTheme()

    LazyRowSettingsItem(
        title = stringResource(R.string.pref_dark_mode),
        description = stringResource(R.string.pref_dark_mode_desc1),
        horizontalArrangement = Arrangement.spacedBy(VerveDoDefaults.contentPadding / 2),
        modifier = modifier
    ) {
        items(items = DarkMode.entries, key = { it.id }) {
            val (contentColor, containerColor) = when (it) {
                DarkMode.FollowSystem -> if (isInDarkTheme) Color.White to Color.Black else Color.Black to Color.White
                DarkMode.Light -> Color.Black to Color.White
                DarkMode.Dark -> Color.White to Color.Black
            }

            DarkModeItem(
                iconRes = it.iconRes,
                name = stringResource(it.nameRes),
                contentColor = contentColor,
                containerColor = containerColor,
                selected = currentDarkMode == it,
                onSelect = { onDarkModeChange(it) })
        }
    }
}