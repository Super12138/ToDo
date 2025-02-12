package cn.super12138.todo.ui.pages.settings.components.licence

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.BasicDialog
import cn.super12138.todo.ui.components.LazyColumnCustomScrollBar
import cn.super12138.todo.utils.VibrationUtils
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.util.htmlReadyLicenseContent

@Composable
fun LicenceList(
    libraries: Libs?,
    state: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val view = LocalView.current
    LazyColumnCustomScrollBar(state = state) {
        LazyColumn(state = state) {
            items(libraries?.libraries ?: listOf()) { library ->
                var openDialog by rememberSaveable { mutableStateOf(false) }

                LicenceItem(
                    library = library,
                    onClick = {
                        val license = library.licenses.firstOrNull()
                        if (!license?.htmlReadyLicenseContent.isNullOrBlank()) {
                            openDialog = true
                        } else if (!license?.url.isNullOrBlank()) {
                            license?.url?.also {
                                try {
                                    uriHandler.openUri(it)
                                } catch (t: Throwable) {
                                    Log.e("Licence", "Failed to open the URL: $it")
                                }
                            }
                        }
                        VibrationUtils.performHapticFeedback(view)
                    },
                )

                BasicDialog(
                    visible = openDialog,
                    title = { Text(library.name) },
                    text = {
                        library.licenses.firstOrNull()?.licenseContent?.let {
                            SelectionContainer {
                                Text(text = it)
                            }
                        }
                    },
                    confirmButton = {
                        FilledTonalButton(onClick = {
                            openDialog = false
                            VibrationUtils.performHapticFeedback(view)
                        }) { Text(stringResource(R.string.action_confirm)) }
                    },
                    onDismissRequest = { openDialog = false }
                )
            }
        }
    }
}