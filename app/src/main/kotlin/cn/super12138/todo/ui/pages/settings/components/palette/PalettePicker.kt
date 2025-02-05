package cn.super12138.todo.ui.pages.settings.components.palette

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.super12138.todo.R
import cn.super12138.todo.constants.Constants
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.large)
            .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.pref_palette_style),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
        )

        Spacer(Modifier.size(8.dp))

        val paletteOptions = remember {
            PaletteStyle.entries.toList()
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(items = paletteOptions, key = { it.name }) { paletteStyle ->
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
}