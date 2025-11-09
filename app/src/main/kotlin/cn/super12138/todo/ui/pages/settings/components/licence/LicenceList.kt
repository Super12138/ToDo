package cn.super12138.todo.ui.pages.settings.components.licence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.BasicDialog
import cn.super12138.todo.ui.components.LazyColumnCustomScrollBar
import cn.super12138.todo.utils.VibrationUtils
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.util.htmlReadyLicenseContent

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LicenceList(
    modifier: Modifier = Modifier,
    libraries: Libs?,
    state: LazyListState = rememberLazyListState()
) {
    val view = LocalView.current
    val uriHandler = LocalUriHandler.current

    LazyColumnCustomScrollBar(
        state = state,
        modifier = modifier
    ) {
        LazyColumn(
            state = state,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = modifier
        ) {
            items(items = libraries?.libraries ?: listOf(), key = { it.artifactId }) { library ->
                var openDialog by rememberSaveable { mutableStateOf(false) }

                LicenceItem(
                    library = library,
                    onClick = {
                        val license = library.licenses.firstOrNull()
                        if (!license?.htmlReadyLicenseContent.isNullOrBlank()) {
                            openDialog = true
                        } else if (!license?.url.isNullOrBlank()) {
                            license.url?.also {
                                try {
                                    uriHandler.openUri(it)
                                } catch (t: Throwable) {
                                    throw Exception("Failed to open licence URL: $it", t)
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
                            Column(Modifier.verticalScroll(rememberScrollState())) {
                                SelectionContainer { Text(text = it) }
                            }
                        }
                    },
                    confirmButton = {
                        FilledTonalButton(
                            onClick = {
                                openDialog = false
                                VibrationUtils.performHapticFeedback(view)
                            },
                            shapes = ButtonDefaults.shapes()
                        ) { Text(stringResource(R.string.action_confirm)) }
                    },
                    onDismissRequest = { openDialog = false }
                )
            }
        }
    }
}