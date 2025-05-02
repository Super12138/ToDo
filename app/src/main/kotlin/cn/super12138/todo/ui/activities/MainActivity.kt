package cn.super12138.todo.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.super12138.todo.ui.components.Konfetti
import cn.super12138.todo.ui.navigation.TodoNavigation
import cn.super12138.todo.ui.theme.DarkMode.Dark
import cn.super12138.todo.ui.theme.DarkMode.FollowSystem
import cn.super12138.todo.ui.theme.DarkMode.Light
import cn.super12138.todo.ui.theme.ToDoTheme
import cn.super12138.todo.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val showConfetti = mainViewModel.showConfetti
            // 深色模式
            val darkTheme = when (mainViewModel.appDarkMode) {
                FollowSystem -> isSystemInDarkTheme()
                Light -> false
                Dark -> true
            }
            // 配置状态栏和底部导航栏的颜色（在用户切换深色模式时）
            // https://github.com/dn0ne/lotus/blob/master/app/src/main/java/com/dn0ne/player/MainActivity.kt#L266
            LaunchedEffect(mainViewModel.appDarkMode) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
            // 阻止截屏相关配置
            LaunchedEffect(mainViewModel.appSecureMode) {
                if (mainViewModel.appSecureMode) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }

            ToDoTheme(
                darkTheme = darkTheme,
                style = mainViewModel.appPaletteStyle,
                contrastLevel = mainViewModel.appContrastLevel.value.toDouble(),
                dynamicColor = mainViewModel.appDynamicColorEnable
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TodoNavigation(
                        viewModel = mainViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                    Konfetti(state = showConfetti)
                }
            }
        }
    }
}