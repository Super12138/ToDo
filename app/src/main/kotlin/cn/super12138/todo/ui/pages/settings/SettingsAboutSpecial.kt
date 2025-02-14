package cn.super12138.todo.ui.pages.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.super12138.todo.R
import cn.super12138.todo.ui.viewmodels.MainViewModel
import cn.super12138.todo.utils.VibrationUtils

@Composable
fun SettingsAboutSpecial(
    viewModel: MainViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    var showBanner by rememberSaveable { mutableStateOf(false) }
    Surface(color = SpecialDefaults.WallColor) {
        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = showBanner,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight / 4)
                        .clip(MaterialTheme.shapes.large)
                        .background(SpecialDefaults.BannerColor)
                        .padding(WindowInsets.statusBars.asPaddingValues())

                ) {
                    Text(
                        text = stringResource(R.string.happy_birthday),
                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black)
                    )
                }
            }
            // 蛋糕
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Cake(
                    onCandleClick = {
                        showBanner = it
                        if (it) viewModel.playConfetti()
                    }
                )
                // 桌子
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(SpecialDefaults.TableHeight)
                        .background(SpecialDefaults.TableColor)
                )
            }
        }
    }
}


@Composable
private fun Cake(
    onCandleClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        Row {
            Candle(onClick = onCandleClick)
        }
        CakePiece(
            width = screenWidth / 4,
            height = SpecialDefaults.Cake2Height,
            color = SpecialDefaults.Cake2Color
        )
        CakePiece(
            width = screenWidth / 3,
            height = SpecialDefaults.Cake1Height,
            color = SpecialDefaults.Cake1Color
        )
    }
}

@Composable
private fun CakePiece(
    width: Dp,
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .width(width)
            .height(height)
            .clip(
                MaterialTheme.shapes.small.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            )
            .background(color)
    )
}

@Composable
fun Candle(
    onClick: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFlame by rememberSaveable { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val view = LocalView.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                showFlame = !showFlame
                VibrationUtils.performHapticFeedback(view)
                onClick(showFlame)
            }
        )
    ) {
        // 火苗
        Box {
            androidx.compose.animation.AnimatedVisibility(
                visible = showFlame,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 2.5.dp)
            ) {
                Box(
                    Modifier
                        .size(10.dp)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    SpecialDefaults.FlameCenterColor,
                                    SpecialDefaults.FlameAroundColor,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            Box(
                Modifier
                    .width(SpecialDefaults.WickWidth)
                    .height(SpecialDefaults.WickHeight)
                    .background(SpecialDefaults.WickColor)
                    .align(Alignment.BottomCenter)
            )
        }
        Box(
            Modifier
                .width(SpecialDefaults.CandleBodyWidth)
                .height(SpecialDefaults.CandleBodyHeight)
                .background(Color(0xFFECCE48))
        )
    }
}

private object SpecialDefaults {
    // Color
    val WallColor = Color(0xFFFFFBEA)
    val TableColor = Color(0xFF412200)
    val Cake1Color = Color(0xFFFFE9A0)
    val Cake2Color = Color(0xFFFFECBF)
    val FlameCenterColor = Color(0xFFFF5100)
    val FlameAroundColor = Color(0xFFF82323)
    val WickColor = Color(0xFF1E1E1E)
    val BannerColor = Color(0xFFFFF1BC)

    // Size
    val TableHeight = 30.dp
    val Cake1Height = 60.dp
    val Cake2Height = 45.dp
    val CandleBodyWidth = 10.dp
    val CandleBodyHeight = 20.dp
    val WickWidth = 1.5.dp
    val WickHeight = 8.dp
}
