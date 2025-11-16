package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.datastore.DataStoreManager
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.Settings
import cn.super12138.todo.ui.pages.settings.components.SwitchSettingsItem
import cn.super12138.todo.ui.pages.settings.components.appearance.contrast.ContrastPicker
import cn.super12138.todo.ui.pages.settings.components.appearance.darkmode.DarkModePicker
import cn.super12138.todo.ui.pages.settings.components.appearance.palette.PalettePicker
import cn.super12138.todo.ui.theme.ContrastLevel
import cn.super12138.todo.ui.theme.DarkMode
import cn.super12138.todo.ui.theme.PaletteStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppearance(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dynamicColor by DataStoreManager.dynamicColorFlow.collectAsState(initial = Constants.PREF_DYNAMIC_COLOR_DEFAULT)
    val darkMode by DataStoreManager.darkModeFlow.collectAsState(initial = Constants.PREF_DARK_MODE_DEFAULT)
    val paletteStyle by DataStoreManager.paletteStyleFlow.collectAsState(initial = Constants.PREF_PALETTE_STYLE_DEFAULT)
    val contrastLevel by DataStoreManager.contrastLevelFlow.collectAsState(initial = Constants.PREF_CONTRAST_LEVEL_DEFAULT)

    val scope = rememberCoroutineScope()
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
                .padding(horizontal = TodoDefaults.screenHorizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Settings {
                SwitchSettingsItem(
                    checked = dynamicColor,
                    leadingIconRes = R.drawable.ic_wand_stars,
                    title = stringResource(R.string.pref_appearance_dynamic_color),
                    description = stringResource(R.string.pref_appearance_dynamic_color_desc),
                    onCheckedChange = { scope.launch { DataStoreManager.setDynamicColor(it) } },
                )

                DarkModePicker(
                    currentDarkMode = { DarkMode.fromId(darkMode) },
                    onDarkModeChange = { scope.launch { DataStoreManager.setDarkMode(it.id) } }
                )

                PalettePicker(
                    currentPalette = { PaletteStyle.fromId(paletteStyle) },
                    onPaletteChange = { scope.launch { DataStoreManager.setPaletteStyle(it.id) } },
                    isDynamicColor = dynamicColor,
                    isDarkMode = DarkMode.fromId(darkMode),
                    contrastLevel = ContrastLevel.fromFloat(contrastLevel)
                )

                ContrastPicker(
                    currentContrast = ContrastLevel.fromFloat(contrastLevel),
                    onContrastChange = { scope.launch { DataStoreManager.setContrastLevel(it.value) } }
                )
            }
        }
    }
}