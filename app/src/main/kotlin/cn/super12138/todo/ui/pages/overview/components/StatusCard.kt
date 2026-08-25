package cn.super12138.todo.ui.pages.overview.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.super12138.todo.ui.VerveDoDefaults
import cn.super12138.todo.ui.theme.shapeByInteraction
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun RoundedCornerCardLarge(
    @DrawableRes iconRes: Int,
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = VerveDoDefaults.Colors.Container,
    shapes: ButtonShapes = VerveDoDefaults.shapes,
    colors: CardColors = CardDefaults.cardColors(containerColor = containerColor),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {}
) {
    val view = LocalView.current

    val pressed by interactionSource.collectIsPressedAsState()
    val shape = shapeByInteraction(shapes, pressed, VerveDoDefaults.shapesDefaultAnimationSpec)

    Surface(
        onClick = {
            VibrationUtils.performHapticFeedback(view)
            onClick()
        },
        modifier = modifier
            .height(VerveDoDefaults.Sizes.overviewCardHeight)
            .semantics { role = Role.Button },
        shape = shape,
        color = colors.containerColor,
        contentColor = colors.contentColor,
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(VerveDoDefaults.screenHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = VerveDoDefaults.contentPadding)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                BasicText(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorProducer { colors.contentColor },
                    autoSize = TextAutoSize.StepBased(
                        MaterialTheme.typography.headlineSmall.fontSize,
                        MaterialTheme.typography.displayMedium.fontSize
                    ),
                )
            }
        }
    }
}