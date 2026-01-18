package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.TopAppBarScaffold
import cn.super12138.todo.ui.components.RoundedScreenContainer
import cn.super12138.todo.ui.pages.settings.components.SettingsContainer
import cn.super12138.todo.ui.pages.settings.components.SettingsItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsMain(
    toAppearancePage: () -> Unit,
    toInterfacePage: () -> Unit,
    toDataPage: () -> Unit,
    toAboutPage: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBarScaffold(
        title = stringResource(R.string.page_settings),
        onBack = onNavigateUp,
        modifier = modifier
    ) { innerPadding ->
        RoundedScreenContainer(Modifier.padding(innerPadding)) {
            SettingsContainer(Modifier.fillMaxSize()) {
                item {
                    SettingsItem(
                        leadingIconRes = R.drawable.ic_palette,
                        title = stringResource(R.string.pref_appearance),
                        description = stringResource(R.string.pref_appearance_desc),
                        onClick = toAppearancePage,
                        topRounded = true
                    )
                }

                item {
                    SettingsItem(
                        leadingIconRes = R.drawable.ic_view_comfy,
                        title = stringResource(R.string.pref_interface_interaction),
                        description = stringResource(R.string.pref_interface_interaction_desc),
                        onClick = toInterfacePage
                    )
                }

                item {
                    SettingsItem(
                        leadingIconRes = R.drawable.ic_dns,
                        title = stringResource(R.string.pref_data),
                        description = stringResource(R.string.pref_data_desc),
                        onClick = toDataPage
                    )
                }

                item {
                    SettingsItem(
                        leadingIconRes = R.drawable.ic_info,
                        title = stringResource(R.string.pref_about),
                        description = stringResource(R.string.pref_about_desc),
                        onClick = toAboutPage,
                        bottomRounded = true
                    )
                }
            }
        }
    }
}