package cn.super12138.todo.ui.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.datastore.DataStoreManager
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.PaletteStyle
import cn.super12138.todo.ui.TodoDefaults
import cn.super12138.todo.ui.components.Konfetti
import cn.super12138.todo.ui.navigation.TodoDestinations
import cn.super12138.todo.ui.navigation.TodoNavigation
import cn.super12138.todo.ui.theme.ToDoTheme
import cn.super12138.todo.ui.viewmodels.MainViewModel
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.configureEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        configureEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val navigationBackStack = mainViewModel.topLevelBackStack
            val showConfetti = mainViewModel.showConfetti
            // 主题
            val dynamicColor by DataStoreManager.dynamicColorFlow.collectAsState(initial = Constants.PREF_DYNAMIC_COLOR_DEFAULT)
            val paletteStyle by DataStoreManager.paletteStyleFlow.collectAsState(initial = Constants.PREF_PALETTE_STYLE_DEFAULT)
            val contrastLevel by DataStoreManager.contrastLevelFlow.collectAsState(initial = Constants.PREF_CONTRAST_LEVEL_DEFAULT)
            val darkMode by DataStoreManager.darkModeFlow.collectAsState(initial = Constants.PREF_DARK_MODE_DEFAULT)
            val secureMode by DataStoreManager.secureModeFlow.collectAsState(initial = Constants.PREF_SECURE_MODE_DEFAULT)
            val hapticFeedback by DataStoreManager.hapticFeedbackFlow.collectAsState(initial = Constants.PREF_HAPTIC_FEEDBACK_DEFAULT)

            // 深色模式
            val darkTheme = when (DarkMode.fromId(darkMode)) {
                DarkMode.FollowSystem -> isSystemInDarkTheme()
                DarkMode.Light -> false
                DarkMode.Dark -> true
            }
            // 配置状态栏和底部导航栏的颜色（在用户切换深色模式时）
            // https://github.com/dn0ne/lotus/blob/master/app/src/main/java/com/dn0ne/player/MainActivity.kt#L266
            LaunchedEffect(darkMode) {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            // 阻止截屏相关配置
            LaunchedEffect(secureMode) {
                if (secureMode) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }

            LaunchedEffect(hapticFeedback) {
                VibrationUtils.setEnabled(hapticFeedback)
            }

            ToDoTheme(
                darkTheme = darkTheme,
                style = PaletteStyle.fromId(paletteStyle),
                contrastLevel = contrastLevel.toDouble(),
                dynamicColor = dynamicColor
            ) {
                Surface(color = TodoDefaults.BackgroundColor) {
                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            TodoDestinations.entries.forEach {
                                val selected = it.route == navigationBackStack.topLevelKey
                                item(
                                    icon = {
                                        Icon(
                                            painter = if (selected) painterResource(it.selectedIcon) else painterResource(
                                                it.icon
                                            ),
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(stringResource(it.label)) },
                                    selected = selected,
                                    onClick = { navigationBackStack.addTopLevel(it.route) }
                                )
                            }
                        },
                        containerColor = TodoDefaults.BackgroundColor
                    ) {
                        TodoNavigation(
                            backStack = navigationBackStack,
                            viewModel = mainViewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Konfetti(state = showConfetti)
                }
            }
        }
    }
}