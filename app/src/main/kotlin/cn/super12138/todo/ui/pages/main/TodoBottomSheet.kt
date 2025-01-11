package cn.super12138.todo.ui.pages.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.ui.pages.main.components.FilterChipGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onSave: (TodoEntity) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.action_add_todo),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.size(25.dp))
            var text by rememberSaveable { mutableStateOf("") }
            var isError by rememberSaveable { mutableStateOf(false) }
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("待办内容") },
                isError = isError,
                supportingText = {
                    AnimatedVisibility(isError) {
                        Text("没有内容")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(5.dp))
            val subjects =
                listOf(
                    "语文",
                    "数学",
                    "英语",
                    "生物",
                    "地理",
                    "物理",
                    "化学",
                    "道法",
                    "历史",
                    "其它"
                )

            var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
            FilterChipGroup(
                items = subjects,
                onSelectedChanged = {
                    selectedItemIndex = it
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onClose
                ) {
                    Text("取消")
                }
                FilledTonalButton(
                    onClick = {
                        if (text.trim().isEmpty()) {
                            isError = true
                            return@FilledTonalButton
                        }
                        isError = false
                        onSave(
                            TodoEntity(
                                content = text,
                                subject = subjects[selectedItemIndex]
                            )
                        )
                    }
                ) {
                    Text("添加")
                }
            }
            Spacer(Modifier.size(30.dp))
        }
    }
}