package cn.super12138.todo.ui.pages.editor.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.utils.VibrationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPrioritySlider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val context = LocalContext.current

    val priorityName = remember { Priority.entries.map { it.displayName(context) } }
    val interactionSource = remember { MutableInteractionSource() }

    Slider(
        value = value(),
        onValueChange = {
            VibrationUtils.performHapticFeedback(view, HapticFeedbackConstants.LONG_PRESS)
            onValueChange(it)
        },
        valueRange = -2f..2f,
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
                        Text(priorityName[Priority.fromFloat(value()).ordinal])
                    }
                },
                interactionSource = interactionSource
            ) {
                SliderDefaults.Thumb(interactionSource)
            }
        },
        modifier = modifier.semantics {
            contentDescription =
                context.getString(R.string.label_priority) + priorityName[Priority.fromFloat(
                    value()
                ).ordinal]
            stateDescription = priorityName[Priority.fromFloat(value()).ordinal]
            liveRegion = LiveRegionMode.Polite
        }
    )
}