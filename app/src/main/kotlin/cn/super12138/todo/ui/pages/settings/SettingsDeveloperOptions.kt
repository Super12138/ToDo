package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.TopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.SettingsContainer
import cn.super12138.todo.ui.pages.settings.components.SettingsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDeveloperOptions(
    toPaddingPage: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBarScaffold(
        title = stringResource(R.string.pref_developer_options),
        onBack = onNavigateUp,
        modifier = modifier,
    ) {
        SettingsContainer(Modifier.fillMaxSize()) {
            item {
                SettingsItem(
                    leadingIconRes = R.drawable.ic_padding,
                    title = stringResource(R.string.pref_padding),
                    onClick = toPaddingPage,
                    topRounded = true,
                    bottomRounded = true
                )
            }
        }
    }
}