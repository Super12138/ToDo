package cn.super12138.todo.ui.pages.settings.components.darkmode

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.ui.pages.settings.components.RowSettingsItem
import cn.super12138.todo.ui.pages.settings.state.rememberPrefIntState

@Composable
fun DarkModePicker(
    onDarkModeChange: (darkMode: DarkMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val isInDarkTheme = isSystemInDarkTheme()

    var darkModeState by rememberPrefIntState(
        Constants.PREF_DARK_MODE,
        Constants.PREF_DARK_MODE_DEFAULT
    )

    RowSettingsItem(
        title = stringResource(R.string.pref_dark_mode),
        description = stringResource(R.string.pref_dark_mode_desc),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            DarkModeItem(
                icon = DarkMode.FollowSystem.icon,
                contentDescription = DarkMode.FollowSystem.getDisplayName(context),
                contentColor = if (isInDarkTheme) Color.White else Color.Black,
                containerColor = if (isInDarkTheme) Color.Black else Color.White,
                selected = DarkMode.fromId(darkModeState) == DarkMode.FollowSystem,
                onSelect = {
                    darkModeState = DarkMode.FollowSystem.id
                    onDarkModeChange(DarkMode.FollowSystem)
                }
            )
        }

        item {
            DarkModeItem(
                icon = DarkMode.Light.icon,
                contentDescription = DarkMode.Light.getDisplayName(context),
                contentColor = Color.Black,
                containerColor = Color.White,
                selected = DarkMode.fromId(darkModeState) == DarkMode.Light,
                onSelect = {
                    darkModeState = DarkMode.Light.id
                    onDarkModeChange(DarkMode.Light)
                }
            )
        }

        item {
            DarkModeItem(
                icon = DarkMode.Dark.icon,
                contentDescription = DarkMode.Dark.getDisplayName(context),
                contentColor = Color.White,
                containerColor = Color.Black,
                selected = DarkMode.fromId(darkModeState) == DarkMode.Dark,
                onSelect = {
                    darkModeState = DarkMode.Dark.id
                    onDarkModeChange(DarkMode.Dark)
                }
            )
        }
    }
}