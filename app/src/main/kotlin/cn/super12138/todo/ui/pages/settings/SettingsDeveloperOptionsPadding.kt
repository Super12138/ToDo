package cn.super12138.todo.ui.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isCaptionBarVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import cn.super12138.todo.utils.VibrationUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsDeveloperOptionsPadding(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Box(
            Modifier
                .safeGesturesPadding()
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                Modifier
                    .safeDrawingPadding()
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                SelectionContainer {
                    Column {
                        DisableSelection {
                            Text("Direction: Left Top Right Bottom\n")
                        }
                        Text("Gestures Padding: \n${WindowInsets.safeGestures}\n")
                        Text("Drawing Padding: \n${WindowInsets.safeDrawing}\n")
                        Text("Caption Bar: ${WindowInsets.isCaptionBarVisible}, ${WindowInsets.captionBar}")
                        Text("Navigation Bar: ${WindowInsets.navigationBars}")
                        DisableSelection {
                            Button(
                                onClick = {
                                    VibrationUtils.performHapticFeedback(view)
                                    onNavigateUp()
                                }
                            ) { Text("Back") }
                        }
                    }
                }
            }

        }
    }
}