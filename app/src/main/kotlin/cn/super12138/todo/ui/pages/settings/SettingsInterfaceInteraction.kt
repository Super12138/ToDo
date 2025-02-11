package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.model.SortingMethod
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.SettingsCategory
import cn.super12138.todo.ui.pages.settings.components.SettingsItem
import cn.super12138.todo.ui.pages.settings.components.SettingsPlainBox
import cn.super12138.todo.ui.pages.settings.components.SettingsRadioDialog
import cn.super12138.todo.ui.pages.settings.components.SettingsRadioOptions
import cn.super12138.todo.ui.pages.settings.components.SwitchSettingsItem
import cn.super12138.todo.ui.viewmodels.MainViewModel
import cn.super12138.todo.utils.VibrationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsInterface(
    viewModel: MainViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showSortingMethodDialog by rememberSaveable { mutableStateOf(false) }
    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_interface_interaction),
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
            SettingsCategory(stringResource(R.string.pref_category_todo_list))

            SwitchSettingsItem(
                key = Constants.PREF_SHOW_COMPLETED,
                default = Constants.PREF_SHOW_COMPLETED_DEFAULT,
                leadingIcon = Icons.Outlined.Checklist,
                title = stringResource(R.string.pref_show_completed),
                description = stringResource(R.string.pref_show_completed_desc),
                onCheckedChange = { viewModel.setShowCompleted(it) },
            )
            SettingsItem(
                leadingIcon = Icons.AutoMirrored.Outlined.Sort,
                title = stringResource(R.string.pref_sorting_method),
                description = viewModel.appSortingMethod.getDisplayName(context),
                onClick = { showSortingMethodDialog = true }
            )

            SettingsCategory(stringResource(R.string.pref_category_global))
            SwitchSettingsItem(
                key = Constants.PREF_SECURE_MODE,
                default = Constants.PREF_SECURE_MODE_DEFAULT,
                leadingIcon = Icons.Outlined.Shield,
                title = stringResource(R.string.pref_secure_mode),
                description = stringResource(R.string.pref_secure_mode_desc),
                onCheckedChange = { viewModel.setSecureMode(it) }
            )
            SwitchSettingsItem(
                key = Constants.PREF_HAPTIC_FEEDBACK,
                default = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT,
                leadingIcon = Icons.Outlined.Vibration,
                title = stringResource(R.string.pref_haptic_feedback),
                description = stringResource(R.string.pref_haptic_feedback_desc),
                onCheckedChange = { VibrationUtils.setEnabled(it) }
            )
            SettingsPlainBox(stringResource(R.string.pref_haptic_feedback_more_info))
        }
    }

    val sortingList = remember {
        SortingMethod.entries.map {
            SettingsRadioOptions(
                id = it.id,
                text = it.getDisplayName(context)
            )
        }
    }
    SettingsRadioDialog(
        key = Constants.PREF_SORTING_METHOD,
        defaultIndex = Constants.PREF_SORTING_METHOD_DEFAULT,
        visible = showSortingMethodDialog,
        title = stringResource(R.string.pref_sorting_method),
        options = sortingList,
        onSelect = { id ->
            viewModel.setSortingMethod(SortingMethod.fromId(id))
        },
        onDismiss = { showSortingMethodDialog = false }
    )
}