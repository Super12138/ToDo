package cn.super12138.todo.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.super12138.todo.logic.model.DarkMode.Dark
import cn.super12138.todo.logic.model.DarkMode.FollowSystem
import cn.super12138.todo.logic.model.DarkMode.Light
import cn.super12138.todo.ui.components.Konfetti
import cn.super12138.todo.ui.navigation.TodoNavigation
import cn.super12138.todo.ui.theme.ToDoTheme
import cn.super12138.todo.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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

            ToDoTheme(
                darkTheme = darkTheme,
                contrastLevel = mainViewModel.appContrastLevel.value.toDouble()
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