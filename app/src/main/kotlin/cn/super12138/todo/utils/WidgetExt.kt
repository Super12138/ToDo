package cn.super12138.todo.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceTheme
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import cn.super12138.todo.logic.model.Priority

object GlanceTypography {
    val defaultColor: ColorProvider
        @Composable get() = GlanceTheme.colors.onSurface
    val titleLarge: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        )
    val titleMedium: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    val labelLarge: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        )
    val labelMedium: TextStyle
        @Composable get() = TextStyle(
            color = defaultColor,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
}

data class FixedColorProvider(val color: Color) : ColorProvider {
    override fun getColor(context: Context): Color = color
}

fun Color.toColorProvider() = FixedColorProvider(this)

@Stable
@Composable
fun Priority.glanceContainerColor(): ColorProvider =
    when (this) {
        Priority.NotUrgent -> GlanceTheme.colors.onSurfaceVariant
        Priority.NotImportant -> GlanceTheme.colors.onSurfaceVariant
        Priority.Default -> GlanceTheme.colors.secondary
        Priority.Important -> GlanceTheme.colors.tertiary
        Priority.Urgent -> GlanceTheme.colors.error
    }
