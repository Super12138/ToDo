package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.DefaultSettingsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMain(
    onNavigateUp: () -> Unit,
    toAboutPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarScaffold(
        title = stringResource(R.string.page_settings),
        scrollBehavior = scrollBehavior,
        onBack = onNavigateUp
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            DefaultSettingsItem(
                leadingIcon = Icons.Outlined.Info,
                title = stringResource(R.string.pref_about),
                description = stringResource(R.string.pref_about_desc),
                onClick = toAboutPage
            )
        }
    }
}