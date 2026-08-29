package cn.super12138.todo.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.ColorUtils
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.Priority
import cn.super12138.todo.logic.model.SortingMethod
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun Int.blend(
    color: Int,
    @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.5f,
): Int = ColorUtils.blendARGB(this, color, fraction)

@Composable
@Stable
fun Priority.containerColor(): Color =
    when (this) {
        Priority.NotUrgent -> MaterialTheme.colorScheme.onSurfaceVariant
        Priority.NotImportant -> MaterialTheme.colorScheme.onSurfaceVariant
        Priority.Default -> MaterialTheme.colorScheme.secondary
        Priority.Important -> MaterialTheme.colorScheme.tertiary
        Priority.Urgent -> MaterialTheme.colorScheme.error
    }

/**
 * 获取部分圆角的形状
 *
 * @param topRounded 顶部是否圆角
 * @param bottomRounded 底部是否圆角
 * @param roundedShape 所需圆角形状
 */
@Composable
fun CornerBasedShape.getPartialRoundedShape(
    topRounded: Boolean,
    bottomRounded: Boolean,
    roundedShape: CornerBasedShape
): CornerBasedShape =
    this.copy(
        topStart = if (topRounded) roundedShape.topStart else this.topStart,
        topEnd = if (topRounded) roundedShape.topEnd else this.topEnd,
        bottomEnd = if (bottomRounded) roundedShape.bottomEnd else this.bottomEnd,
        bottomStart = if (bottomRounded) roundedShape.bottomStart else this.bottomStart,
    )

/**
 * 绘制渐变边缘遮罩
 *
 * @param edgeWidth 渐变边缘宽度
 * @param maskColor 遮罩颜色
 * @param leftEdge 是否在左侧边缘添加遮罩（否即在右侧边缘添加）
 */
fun ContentDrawScope.drawFadedEdge(
    edgeWidth: Dp,
    maskColor: Color,
    leftEdge: Boolean
) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush =
            Brush.horizontalGradient(
                colors = listOf(Color.Transparent, maskColor),
                startX = if (leftEdge) 0f else size.width,
                endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
            ),
        blendMode = BlendMode.DstIn
    )
}

/**
 * 将时间戳转换为本地日期字符串
 *
 * @receiver Long? 时间戳（单位为毫秒）或 null
 * @return String 格式化后的日期字符串。如果为传入参数为null则返回空字符串，反之格式为 “yyyy-MM-dd”
 */
fun Long?.toLocalDateString(): String {
    if (this == null) return ""
    val date = Date(this)
    return dateFormat.format(date)
}

/**
 * 将时间戳转换为相对时间字符串
 *
 * @receiver Long? 时间戳（单位为毫秒）或 null
 * @param context 上下文，用于获取字符串资源
 * @return String 格式化后的相对时间字符串。如果为传入参数为null则返回空字符串，反之根据时间差返回相应的字符串，如“今天”、“明天”、“3天后”、“2周后”、“1个月后”、“1年后”等
 */
fun Long?.toRelativeTimeString(context: Context): String {
    if (this == null) return ""
    val today = SystemUtils.getStartOfDayMillis(0)

    return when (this - today) {
        in 0L..0L -> context.getString(R.string.time_today)

        // 将来的时间
        in 1.days.inWholeMilliseconds..1.days.inWholeMilliseconds -> context.getString(R.string.time_tomorrow)
        in 2.days.inWholeMilliseconds..6.days.inWholeMilliseconds -> context.getString(
            R.string.time_in_days,
            ((this - today).milliseconds.inWholeDays).toInt()
        )

        in 7.days.inWholeMilliseconds..29.days.inWholeMilliseconds -> context.getString(
            R.string.time_in_weeks,
            ((this - today).milliseconds.inWholeDays / 7).toInt()
        )

        in 30.days.inWholeMilliseconds..364.days.inWholeMilliseconds -> context.getString(
            R.string.time_in_months,
            ((this - today).milliseconds.inWholeDays / 30).toInt()
        )

        in 365.days.inWholeMilliseconds..Long.MAX_VALUE -> context.getString(
            R.string.time_in_years,
            ((this - today).milliseconds.inWholeDays / 365).toInt()
        )

        // 过去的时间
        in (-1).days.inWholeMilliseconds..(-1).days.inWholeMilliseconds -> context.getString(R.string.time_yesterday)
        in (-6).days.inWholeMilliseconds..(-2).days.inWholeMilliseconds -> context.getString(
            R.string.time_days_ago,
            (-(this - today).milliseconds.inWholeDays).toInt()
        )

        in (-29).days.inWholeMilliseconds..(-7).days.inWholeMilliseconds -> context.getString(
            R.string.time_weeks_ago,
            (-(this - today).milliseconds.inWholeDays / 7).toInt()
        )

        in (-364).days.inWholeMilliseconds..(-30).days.inWholeMilliseconds -> context.getString(
            R.string.time_months_ago,
            (-(this - today).milliseconds.inWholeDays / 30).toInt()
        )

        in Long.MIN_VALUE..(-365).days.inWholeMilliseconds -> context.getString(
            R.string.time_years_ago,
            (-(this - today).milliseconds.inWholeDays / 365).toInt()
        )

        else -> context.getString(R.string.time_today)
    }
}

fun Long.toLocalDate(): LocalDate {
    val date = Date(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return LocalDate.of(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

@Composable
fun disabledContentColor(alpha: Float = 0.38f): Color =
    MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)

@Composable
fun disabledContainerColor(alpha: Float = 0.12f): Color =
    MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)

fun List<TaskEntity>.sort(sortingMethod: SortingMethod): List<TaskEntity> = when (sortingMethod) {
    SortingMethod.Sequential -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted } // 必须先要按照是否完成排序
            .thenBy { it.id }
    )

    SortingMethod.Category -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted }
            .thenBy { it.category }
    )

    SortingMethod.Priority -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted }
            .thenByDescending { it.priority }
            .thenBy(nullsLast()) { it.dueDateMillis }
    ) // 优先级高的在前

    SortingMethod.Completion -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted }
            .thenBy { it.category }
            .thenByDescending { it.priority }
    ) // 未完成的在前
    SortingMethod.AlphabeticalAscending -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted }
            .thenBy { it.content }
            .thenByDescending { it.priority }
    )

    SortingMethod.AlphabeticalDescending -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted }
            .thenByDescending { it.content }
            .thenByDescending { it.priority }
    )

    SortingMethod.DueDate -> this.sortedWith(
        comparator = compareBy<TaskEntity> { it.isCompleted }
            // 确保未设置截止日期的任务在最下头
            .thenBy(nullsLast()) { it.dueDateMillis }
    )
}

@Composable
fun Boolean.keyColorBasedOnDynamicColor() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && this) {
        colorResource(id = android.R.color.system_accent1_500)
    } else {
        Color(0xFF0061A4)
    }

@Composable
infix fun Int.toggleButtonShapesIn(list: List<Any>) = when (this) {
    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
    list.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
}

@Composable
fun DarkMode.isDark() = when (this) {
    DarkMode.FollowSystem -> isSystemInDarkTheme()
    DarkMode.Light -> false
    DarkMode.Dark -> true
}

@Suppress("NOTHING_TO_INLINE")
inline fun Int.toColor(): Color = Color(this)

// https://github.com/jordond/MaterialKolor/blob/main/material-kolor/src/commonMain/kotlin/com/materialkolor/DynamicMaterialTheme.kt
@Composable
fun Color.animate(animationSpec: AnimationSpec<Color> = spring()): Color =
    animateColorAsState(this, animationSpec).value

// https://github.com/hushenghao/AndroidEasterEggs/blob/main/core/theme/src/main/java/com/dede/android_eggs/views/theme/Theme.kt#L21
/**
 * 让颜色变暗
 */
fun Color.darken(fraction: Float = 0.5f): Color =
    Color(this.toArgb().blend(Color.Black.toArgb(), fraction))

fun Color.replace(color: Color): Color = color
fun Color.replace(color: Int): Color = color.toColor()

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, duration).show()
