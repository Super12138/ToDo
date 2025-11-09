package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ViewComfy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.Settings
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarScaffold(
        title = stringResource(R.string.page_settings),
        scrollBehavior = scrollBehavior,
        onBack = onNavigateUp,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = TodoDefaults.screenHorizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Settings {
                SettingsItem(
                    leadingIcon = Icons.Outlined.ColorLens,
                    title = stringResource(R.string.pref_appearance),
                    description = stringResource(R.string.pref_appearance_desc),
                    onClick = toAppearancePage
                )
                SettingsItem(
                    leadingIcon = Icons.Outlined.ViewComfy,
                    title = stringResource(R.string.pref_interface_interaction),
                    description = stringResource(R.string.pref_interface_interaction_desc),
                    onClick = toInterfacePage
                )
                SettingsItem(
                    leadingIcon = Icons.Outlined.Dns,
                    title = stringResource(R.string.pref_data),
                    description = stringResource(R.string.pref_data_desc),
                    onClick = toDataPage
                )
                SettingsItem(
                    leadingIcon = Icons.Outlined.Info,
                    title = stringResource(R.string.pref_about),
                    description = stringResource(R.string.pref_about_desc),
                    onClick = toAboutPage
                )

            }
        }
    }
}