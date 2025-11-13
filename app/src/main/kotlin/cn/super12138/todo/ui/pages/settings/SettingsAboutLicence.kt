package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.licence.LicenceList
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAboutLicence(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_licence),
        scrollBehavior = scrollBehavior,
        onBack = onNavigateUp,
        modifier = modifier
    ) { innerPadding ->
        val libraries by produceLibraries(R.raw.aboutlibraries)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = TodoDefaults.screenHorizontalPadding)
                .clip(MaterialTheme.shapes.extraLarge)
        ) {
            val listState = rememberLazyListState()
            LicenceList(
                libraries = libraries,
                state = listState
            )
        }
    }
}