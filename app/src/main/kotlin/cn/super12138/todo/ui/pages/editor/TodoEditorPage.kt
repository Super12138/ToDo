package cn.super12138.todo.ui.pages.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.logic.model.Subjects
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.AnimatedExtendedFloatingActionButton
import cn.super12138.todo.ui.components.FilterChipGroup
import cn.super12138.todo.ui.components.LargeTopAppBarScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditorPage(
    toDo: TodoEntity? = null,
    onSave: (TodoEntity) -> Unit,
    onDelete: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateUp()
    }
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var text by rememberSaveable { mutableStateOf(toDo?.content ?: "") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(toDo?.subject ?: 0) }
    var sliderPosition by rememberSaveable { mutableFloatStateOf(toDo?.priority ?: 0f) }

    LargeTopAppBarScaffold(
        title = stringResource(if (toDo != null) R.string.title_edit_task else R.string.action_add_task),
        scrollBehavior = scrollBehavior,
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (toDo !== null) {
                    AnimatedExtendedFloatingActionButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.action_delete)
                            )
                        },
                        text = { Text(stringResource(R.string.action_delete)) },
                        expanded = true,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        onClick = onDelete
                    )
                }
                AnimatedExtendedFloatingActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Save,
                            contentDescription = stringResource(R.string.action_save)
                        )
                    },
                    text = { Text(stringResource(R.string.action_save)) },
                    expanded = true,
                    onClick = {
                        if (text.trim().isEmpty()) {
                            isError = true
                            return@AnimatedExtendedFloatingActionButton
                        }

                        isError = false
                        onSave(
                            TodoEntity(
                                content = text,
                                subject = selectedItemIndex,
                                isCompleted = toDo?.isCompleted ?: false,
                                priority = sliderPosition,
                                id = toDo?.id ?: 0
                            )
                        )
                    }
                )
            }
        },
        onBack = onNavigateUp,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = TodoDefaults.screenPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.placeholder_add_todo)) },
                isError = isError,
                supportingText = {
                    AnimatedVisibility(isError) {
                        Text(stringResource(R.string.error_no_task_content))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(5.dp))

            val subjects = remember {
                Subjects.entries.map {
                    it.getDisplayName(context)
                }
            }
            Text(
                text = stringResource(R.string.label_subject),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.size(5.dp))

            FilterChipGroup(
                items = subjects,
                defaultSelectedItemIndex = toDo?.subject ?: 0,
                onSelectedChanged = {
                    selectedItemIndex = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(10.dp))

            Text(
                text = stringResource(R.string.label_priority),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.size(5.dp))

            val interactionSource = remember { MutableInteractionSource() }

            Slider(
                modifier = Modifier.semantics {
                    contentDescription = context.getString(R.string.label_priority)
                },
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = -10f..10f,
                steps = 3,
                interactionSource = interactionSource,
                thumb = {
                    Label(
                        label = {
                            PlainTooltip(
                                modifier = Modifier
                                    .sizeIn(45.dp, 25.dp)
                                    .wrapContentWidth()
                            ) {
                                Text(Priority.fromFloat(sliderPosition).getDisplayName(context))
                            }
                        },
                        interactionSource = interactionSource
                    ) {
                        SliderDefaults.Thumb(interactionSource)
                    }
                }
            )

            Spacer(Modifier.size(40.dp))
        }
    }
}