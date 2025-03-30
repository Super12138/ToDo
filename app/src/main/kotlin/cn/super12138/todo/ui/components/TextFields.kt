package cn.super12138.todo.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    options: List<String>,
    state: TextFieldState = rememberTextFieldState(),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    interactionSource: MutableInteractionSource? = null,
    textFieldShape: Shape = TextFieldDefaults.shape,
    menuShape: Shape = MenuDefaults.shape,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors()
) {
    // 用户在文本框中输入的文字可以被用来过滤预先提供的选项 can be used to filter the options.
    // 这里使用了字符串子序列匹配
    val filteredOptions = options.filteredBy(state.text)

    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && filteredOptions.isNotEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = setExpanded,
    ) {
        TextField(
            // `menuAnchor` Modifier 必须传入到文本框以保证可以处理展开/收缩菜单的情况。
            // （一个可编辑的文本框的锚点类型是 `PrimaryEditable`）
            modifier =
                modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            state = state,
            lineLimits = TextFieldLineLimits.SingleLine,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = {
                RotatableArrowIcon(
                    expanded = expanded,
                    // 如果一个文本框是可以编辑的，推荐将尾部图标的 `menuAnchor` 类型设置为 `SecondaryEditable`。
                    // 这有利于提供一个无需输入文本即可选择菜单项的无障碍服务。
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable),
                )
            },
            prefix = prefix,
            suffix = suffix,
            placeholder = placeholder,
            supportingText = supportingText,
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            shape = textFieldShape,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            interactionSource = interactionSource,
            colors = colors,
        )

        ExposedDropdownMenu(
            modifier = Modifier.heightIn(max = 280.dp),
            expanded = expanded,
            shape = menuShape,
            onDismissRequest = { setExpanded(false) },
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        state.setTextAndPlaceCursorAtEnd(option.text)
                        setExpanded(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun RotatableArrowIcon(
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val rotateDegree by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "TextField Arrow Rotate Animation"
    )

    Icon(
        imageVector = Icons.Outlined.ArrowDropDown,
        contentDescription = null,
        modifier = modifier.rotate(rotateDegree)
    )
}

/**
 * Returns the elements of [this] list that contain [text] as a subsequence, with the subsequence
 * underlined as an [AnnotatedString].
 *
 * 返回 [this] 列表中包含 [text] 子序列的元素，并将这个子序列以下划线形式标注为 [AnnotatedString]
 */
private fun List<String>.filteredBy(text: CharSequence): List<AnnotatedString> {
    fun underlineSubsequence(needle: CharSequence, haystack: String): AnnotatedString? {
        return buildAnnotatedString {
            var i = 0
            for (char in needle) {
                val start = i
                haystack.indexOf(char, startIndex = i, ignoreCase = true).let {
                    if (it < 0) return null else i = it
                }
                append(haystack.substring(start, i))
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(haystack[i])
                }
                i += 1
            }
            append(haystack.substring(i, haystack.length))
        }
    }
    return this.mapNotNull { option -> underlineSubsequence(text, option) }
}
