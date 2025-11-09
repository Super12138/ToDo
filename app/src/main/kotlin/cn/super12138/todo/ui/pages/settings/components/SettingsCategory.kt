package cn.super12138.todo.ui.pages.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import cn.super12138.todo.ui.TodoDefaults

@Composable
fun SettingsCategory(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = TodoDefaults.settingsItemVerticalPadding / 2,
                start = TodoDefaults.settingsItemHorizontalPadding,
                end = TodoDefaults.settingsItemHorizontalPadding,
                bottom = TodoDefaults.settingsItemVerticalPadding / 4,
            )
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}