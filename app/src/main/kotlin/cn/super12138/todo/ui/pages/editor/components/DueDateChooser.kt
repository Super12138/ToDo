package cn.super12138.todo.ui.pages.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.utils.SystemUtils
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.toLocalDateString
import java.time.LocalDate

@Composable
fun DueDateChooser(
    dateMillis: Long?,
    onDateChange: (Long?) -> Unit
) {
    val dueDateItems = listOf(
        DueDateItem(label = "Not specific", dueDate = null),
        DueDateItem(label = "Today", dueDate = SystemUtils.getTodayEightAM()),
        DueDateItem(label = "Tomorrow", dueDate = LocalDate.now().plusDays(1)),
        DueDateItem(label = "Next Week", dueDate = LocalDate.now().plusWeeks(1)),
        DueDateItem(
            label = stringResource(R.string.label_customization),
            dueDate = null,
            isCustom = true
        )
    )

    val view = LocalView.current

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)

    var openDialog by remember { mutableStateOf(false) }
    val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedItem by rememberSaveable { mutableStateOf(if (dateMillis != null) dueDateItems.last() else dueDateItems.first()) }

    ExposedDropdownMenu(
        expanded = menuExpanded,
        onExpandedChange = { menuExpanded = it },
        items = dueDateItems,
        selectedItem = selectedItem,
        onSelectedItemChange = {
            selectedItem = it
            if (it.isCustom) openDialog = true
        },
        specificDateMillis = dateMillis
    )

    if (openDialog) {
        DatePickerDialog(
            content = {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                )
            },
            confirmButton = {
                FilledTonalButton(
                    enabled = confirmEnabled,
                    onClick = {
                        VibrationUtils.performHapticFeedback(view)
                        onDateChange(datePickerState.selectedDateMillis)
                        openDialog = false
                    },
                    shapes = ButtonDefaults.shapes(),
                ) {
                    Text(stringResource(R.string.action_confirm))
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(VerveDoDefaults.contentPadding)) {
                    TextButton(
                        onClick = {
                            VibrationUtils.performHapticFeedback(view)
                            datePickerState.selectedDateMillis = null
                        },
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text(stringResource(R.string.action_clear))
                    }
                    TextButton(
                        onClick = {
                            VibrationUtils.performHapticFeedback(view)
                            openDialog = false
                        },
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            },
            onDismissRequest = { openDialog = false }
        )
    }
}

data class DueDateItem(
    val label: String,
    val dueDate: Long? = null,
    val isCustom: Boolean = false
)

@Composable
private fun ExposedDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<DueDateItem>,
    selectedItem: DueDateItem,
    onSelectedItemChange: (DueDateItem) -> Unit,
    modifier: Modifier = Modifier,
    specificDateMillis: Long? = null,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        val selectedText = buildString {
            append(selectedItem.label)

            if (selectedItem.isCustom) {
                specificDateMillis?.let {
                    append(" ")
                    append(it.toLocalDateString())
                }
            }
        }
        TextField(
            value = selectedText,
            onValueChange = {},
            label = { Text(stringResource(R.string.label_due_date)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            readOnly = true,
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            containerColor = MenuDefaults.groupStandardContainerColor,
            shape = MenuDefaults.standaloneGroupShape,
        ) {
            val optionCount = items.size
            items.forEachIndexed { index, option ->
                DropdownMenuItem(
                    shapes = MenuDefaults.itemShape(index, optionCount),
                    text = { Text(option.label, style = MaterialTheme.typography.bodyLarge) },
                    selected = option == selectedItem,
                    onClick = {
                        onExpandedChange(false)
                        onSelectedItemChange(option)
                    },
                    selectedLeadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                            contentDescription = null,
                        )
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
