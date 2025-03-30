package cn.super12138.todo.ui.pages.settings.components.subject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.utils.VibrationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectEditorBottomSheet(
    modifier: Modifier = Modifier,
    subject: String = "",
    visible: Boolean,
    onSave: (String) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TodoDefaults.screenPadding)
            ) {
                val subjectState = rememberTextFieldState(initialText = subject)
                var isError by rememberSaveable { mutableStateOf(false) }

                TextField(
                    state = subjectState,
                    placeholder = { Text("输入学科") },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    isError = isError,
                    supportingText = {
                        AnimatedVisibility(isError) {
                            Text(stringResource(R.string.error_no_content_entered))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.requiredHeight(20.dp))
                Button(
                    onClick = {
                        VibrationUtils.performHapticFeedback(view)
                        if (subjectState.text.isBlank()) {
                            isError = true
                        } else {
                            isError = false
                            // 注意：如果在 onDismissRequest 之外提供关闭 BottomSheet 的逻辑，则必须额外处理所需的状态清理（如有需要）。
                            scope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onSave(subjectState.text.toString())
                                        onDismissRequest()
                                    }
                                }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.action_save))
                }
            }
        }
    }
}