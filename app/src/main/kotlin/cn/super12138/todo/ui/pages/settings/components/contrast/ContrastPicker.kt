package cn.super12138.todo.ui.pages.settings.components.contrast

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.model.ContrastLevel
import cn.super12138.todo.ui.pages.settings.components.MoreContentSettingsItem
import cn.super12138.todo.ui.pages.settings.state.rememberPrefFloatState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContrastPicker(
    onContrastChange: (ContrastLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    MoreContentSettingsItem(
        title = stringResource(R.string.pref_contrast_level),
        description = stringResource(R.string.pref_contrast_level_desc)
    ) {
        var contrastState by rememberPrefFloatState(
            Constants.PREF_CONTRAST_LEVEL,
            Constants.PREF_CONTRAST_LEVEL_DEFAULT
        )
        val interactionSource = remember { MutableInteractionSource() }

        Slider(
            modifier = Modifier.semantics {
                contentDescription = context.getString(R.string.tip_change_contrast_level)
            },
            value = contrastState,
            onValueChange = {
                contrastState = it
                onContrastChange(ContrastLevel.fromFloat(it))
            },
            valueRange = -1f..1f,
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
                            Text(ContrastLevel.fromFloat(contrastState).getDisplayName(context))
                        }
                    },
                    interactionSource = interactionSource
                ) {
                    SliderDefaults.Thumb(interactionSource)
                }
            }
        )
    }
}