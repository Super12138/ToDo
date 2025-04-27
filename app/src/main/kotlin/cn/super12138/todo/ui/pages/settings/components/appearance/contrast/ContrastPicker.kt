package cn.super12138.todo.ui.pages.settings.components.appearance.contrast

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.model.ContrastLevel
import cn.super12138.todo.ui.pages.settings.components.MoreContentSettingsItem
import cn.super12138.todo.ui.pages.settings.state.rememberPrefFloatState
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun ContrastPicker(
    onContrastChange: (ContrastLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val context = LocalContext.current
    MoreContentSettingsItem(
        title = stringResource(R.string.pref_contrast_level),
        description = stringResource(R.string.pref_contrast_level_desc),
        modifier = modifier
    ) {
        var contrastState by rememberPrefFloatState(
            Constants.PREF_CONTRAST_LEVEL,
            Constants.PREF_CONTRAST_LEVEL_DEFAULT
        )

        Slider(
            modifier = Modifier.semantics {
                contentDescription = context.getString(R.string.tip_change_contrast_level)
            },
            value = contrastState,
            onValueChange = {
                VibrationUtils.performHapticFeedback(view, HapticFeedbackConstants.LONG_PRESS)
                contrastState = it
                onContrastChange(ContrastLevel.fromFloat(it))
            },
            valueRange = -1f..1f,
            steps = 3,
        )

        Spacer(Modifier.size(5.dp))

        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.contrast_very_low))
                Text(stringResource(R.string.contrast_low))
                Text(stringResource(R.string.contrast_default))
                Text(stringResource(R.string.contrast_high))
                Text(stringResource(R.string.contrast_very_high))
            }
        }
    }
}