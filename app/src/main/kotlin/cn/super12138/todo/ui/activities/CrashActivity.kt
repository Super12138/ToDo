package cn.super12138.todo.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import cn.super12138.todo.R
import cn.super12138.todo.constants.GlobalValues
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.DarkMode.Dark
import cn.super12138.todo.logic.model.DarkMode.FollowSystem
import cn.super12138.todo.logic.model.DarkMode.Light
import cn.super12138.todo.ui.pages.crash.CrashPage
import cn.super12138.todo.ui.theme.PaletteStyle
import cn.super12138.todo.ui.theme.ToDoTheme

class CrashActivity : ComponentActivity() {
    companion object {
        const val BRAND_PREFIX = "Brand:      "
        const val MODEL_PREFIX = "Model:      "
        const val DEVICE_SDK_PREFIX = "Device SDK: "
        const val CRASH_TIME_PREFIX = "Crash time: "
        const val BEGINNING_CRASH = "======beginning of crash======"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        super.onCreate(savedInstanceState)

        val crashLogs = intent.getStringExtra("crash_logs")

        setContent {
            val darkMode = DarkMode.fromId(GlobalValues.darkMode)
            val darkTheme = when (darkMode) {
                FollowSystem -> isSystemInDarkTheme()
                Light -> false
                Dark -> true
            }
            // 配置状态栏和底部导航栏的颜色（在用户切换深色模式时）
            // https://github.com/dn0ne/lotus/blob/master/app/src/main/java/com/dn0ne/player/MainActivity.kt#L266
            LaunchedEffect(darkMode) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            ToDoTheme(
                darkTheme = darkTheme,
                style = PaletteStyle.fromId(GlobalValues.paletteStyle),
                contrastLevel = GlobalValues.contrastLevel.toDouble(),
                dynamicColor = GlobalValues.dynamicColor
            ) {
                CrashPage(
                    crashLog = crashLogs ?: stringResource(R.string.tip_no_crash_logs),
                    exitApp = { finishAffinity() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}