package cn.super12138.todo.ui.pages.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold
import cn.super12138.todo.ui.components.LazyColumnCustomScrollBar
import cn.super12138.todo.ui.pages.settings.components.subject.SubjectEditorBottomSheet
import cn.super12138.todo.ui.pages.settings.components.subject.SubjectItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDataSubject(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val isExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    var showEditor by rememberSaveable { mutableStateOf(false) }
    var currentSubject by rememberSaveable { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarScaffold(
        title = stringResource(R.string.pref_subject_management),
        onBack = onNavigateUp,
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            AnimatedExtendedFloatingActionButton(
                expanded = isExpanded,
                icon = Icons.Outlined.Add,
                text = stringResource(R.string.action_add_subject),
                onClick = {
                    currentSubject = ""
                    showEditor = true
                }
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        val list = (1..500).toList()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumnCustomScrollBar(state = listState) {
                LazyColumn(state = listState) {
                    items(items = list, key = { it }) {
                        SubjectItem(
                            name = "$it",
                            onClick = {
                                currentSubject = it.toString()
                                showEditor = true
                            },
                            onDelete = { }
                        )
                    }
                }
            }
        }
    }

    SubjectEditorBottomSheet(
        visible = showEditor,
        subject = currentSubject,
        onSave = {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        },
        onDismissRequest = { showEditor = false },
        sheetState = sheetState
    )
}