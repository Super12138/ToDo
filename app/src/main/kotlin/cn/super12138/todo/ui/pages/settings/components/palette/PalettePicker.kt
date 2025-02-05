package cn.super12138.todo.ui.pages.settings.components.palette

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.ui.pages.settings.components.RowSettingsItem
import cn.super12138.todo.ui.pages.settings.state.rememberPrefIntState
import cn.super12138.todo.ui.theme.PaletteStyle

@Composable
fun PalettePicker(
    onPaletteChange: (paletteStyle: PaletteStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    var paletteState by rememberPrefIntState(
        Constants.PREF_PALETTE_STYLE,
        Constants.PREF_PALETTE_STYLE_DEFAULT
    )

    val paletteOptions = remember {
        PaletteStyle.entries.toList()
    }

    RowSettingsItem(
        title = stringResource(R.string.pref_palette_style),
        description = stringResource(R.string.pref_palette_style_desc),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = paletteOptions, key = { it.id }) { paletteStyle ->
            PaletteItem(
                paletteStyle = paletteStyle,
                selected = PaletteStyle.fromId(paletteState) == paletteStyle,
                onSelect = {
                    paletteState = paletteStyle.id
                    onPaletteChange(paletteStyle)
                }
            )
        }
    }
}