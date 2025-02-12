package cn.super12138.todo.ui.pages.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Person4
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.icons.GithubIcon
import cn.super12138.todo.ui.pages.settings.components.SettingsItem
import cn.super12138.todo.utils.SystemUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAbout(
    toSpecialPage: () -> Unit,
    toLicencePage: () -> Unit,
    onNavigateUp: () -> Unit,
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
            var clickCount by remember { mutableIntStateOf(0) }
            SettingsItem(
                leadingIcon = Icons.Outlined.Numbers,
                title = stringResource(R.string.pref_app_version),
                description = SystemUtils.getAppVersion(context),
                onClick = {
                    clickCount++
                    if (clickCount == 5) {
                        if ((System.currentTimeMillis() % 2) == 0.toLong()) {
                            toSpecialPage()
                        } else {
                            Toast.makeText(context, "✨", Toast.LENGTH_SHORT).show()
                        }
                        clickCount = 0
                    }
                }
            )
            SettingsItem(
                leadingIcon = Icons.Outlined.Person4,
                title = stringResource(R.string.pref_developer),
                description = stringResource(R.string.developer_name),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.DEVELOPER_GITHUB))
                    context.startActivity(intent)
                }
            )
            SettingsItem(
                leadingIcon = GithubIcon,
                title = stringResource(R.string.pref_view_on_github),
                description = stringResource(R.string.pref_view_on_github_desc),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GITHUB_REPO))
                    context.startActivity(intent)
                }
            )
            SettingsItem(
                leadingIcon = Icons.Outlined.Balance,
                title = stringResource(R.string.pref_licence),
                description = stringResource(R.string.pref_licence_desc),
                onClick = toLicencePage
            )
        }
    }
}