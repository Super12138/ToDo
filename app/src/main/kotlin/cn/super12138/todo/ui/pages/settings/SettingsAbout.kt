package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.SettingsItem
import cn.super12138.todo.utils.getAppVersion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAbout(
    onNavigateUp: () -> Unit,
    toLicencePage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_about),
        onBack = onNavigateUp,
        scrollBehavior = scrollBehavior,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsItem(
                title = stringResource(R.string.pref_app_version),
                description = getAppVersion(context),
                enableClick = false
            )
            SettingsItem(
                title = stringResource(R.string.pref_developer),
                description = stringResource(R.string.developer_name),
                enableClick = false
            )
            SettingsItem(
                title = stringResource(R.string.pref_licence),
                description = stringResource(R.string.pref_licence_desc),
                onClick = toLicencePage
            )
        }
    }
}