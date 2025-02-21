package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.SwitchSettingsItem
import cn.super12138.todo.ui.pages.settings.components.contrast.ContrastPicker
import cn.super12138.todo.ui.pages.settings.components.darkmode.DarkModePicker
import cn.super12138.todo.ui.pages.settings.components.palette.PalettePicker
import cn.super12138.todo.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppearance(
    viewModel: MainViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_appearance),
        onBack = onNavigateUp,
        scrollBehavior = scrollBehavior,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SwitchSettingsItem(
                key = Constants.PREF_DYNAMIC_COLOR,
                default = Constants.PREF_DYNAMIC_COLOR_DEFAULT,
                leadingIcon = Icons.Outlined.ColorLens,
                title = stringResource(R.string.pref_appearance_dynamic_color),
                description = stringResource(R.string.pref_appearance_dynamic_color_desc),
                onCheckedChange = { viewModel.setDynamicColor(it) },
            )

            DarkModePicker(onDarkModeChange = { viewModel.setDarkMode(it) })

            PalettePicker(
                isDarkMode = viewModel.appDarkMode,
                contrastLevel = viewModel.appContrastLevel,
                onPaletteChange = { viewModel.setPaletteStyle(it) }
            )

            ContrastPicker(onContrastChange = { viewModel.setContrastLevel(it) })
        }
    }
}