package cn.super12138.todo.ui.pages.settings

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.activities.MainActivity
import cn.super12138.todo.ui.components.ConfirmDialog
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.pages.settings.components.Settings
import cn.super12138.todo.ui.pages.settings.components.SettingsCategory
import cn.super12138.todo.ui.pages.settings.components.SettingsItem
import cn.super12138.todo.ui.viewmodels.MainViewModel
import cn.super12138.todo.utils.SystemUtils
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsData(
    viewModel: MainViewModel,
    toCategoryManager: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var showRestoreDialog by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip"),
        onResult = {
            if (it != null) {
                viewModel.backupAppData(
                    uri = it,
                    context = context,
                    onResult = { success ->
                        if (success) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.tip_backup_success)
                                )
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.tip_backup_failed)
                                )
                            }
                        }
                    }
                )
            }
        }
    )

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = {
            if (it != null) {
                viewModel.restoreAppData(
                    uri = it,
                    context = context,
                    onResult = { success ->
                        if (success) {
                            showRestoreDialog = true
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.tip_restore_failed)
                                )
                            }
                        }
                    }
                )
            }
        }
    )

    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_data),
        onBack = onNavigateUp,
        scrollBehavior = scrollBehavior,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = TodoDefaults.screenHorizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsCategory(stringResource(R.string.pref_category_data_management))
            Settings {
                SettingsItem(
                    leadingIcon = Icons.Outlined.FileDownload,
                    title = stringResource(R.string.pref_backup),
                    description = stringResource(R.string.pref_backup_desc),
                    onClick = {
                        backupLauncher.launch("Todo-backup-${SystemUtils.getTime()}.zip")
                    }
                )
                SettingsItem(
                    leadingIcon = Icons.Outlined.FileUpload,
                    title = stringResource(R.string.pref_restore),
                    description = stringResource(R.string.pref_restore_desc),
                    onClick = {
                        restoreLauncher.launch(arrayOf("application/zip"))
                    }
                )
            }
            SettingsCategory(stringResource(R.string.pref_category_category_management))
            Settings {
                SettingsItem(
                    leadingIcon = Icons.Outlined.Category,
                    title = stringResource(R.string.pref_category_category_management),
                    description = stringResource(R.string.pref_category_management_desc),
                    onClick = toCategoryManager
                )
            }
        }
    }
    ConfirmDialog(
        visible = showRestoreDialog,
        icon = Icons.Outlined.RestartAlt,
        title = stringResource(R.string.tip_tips),
        text = stringResource(R.string.tip_restore_success),
        showDismissButton = false,
        onConfirm = { restartApp(context) },
        onDismiss = { showRestoreDialog = false },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

/**
 * 重启应用
 * @param context 上下文
 */
private fun restartApp(context: Context) {
    val intent = Intent(
        context,
        MainActivity::class.java
    ).apply {
        flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    exitProcess(0)
}