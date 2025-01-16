package cn.super12138.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.super12138.todo.ui.pages.main.MainPage
import cn.super12138.todo.ui.pages.settings.SettingsAbout
import cn.super12138.todo.ui.pages.settings.SettingsAboutLicence
import cn.super12138.todo.ui.pages.settings.SettingsMain
import cn.super12138.todo.ui.theme.materialSharedAxisXIn
import cn.super12138.todo.ui.theme.materialSharedAxisXOut
import cn.super12138.todo.ui.viewmodels.MainViewModel

private const val INITIAL_OFFSET_FACTOR = 0.10f

@Composable
fun TodoNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = TodoScreen.Main.name,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            materialSharedAxisXIn(
                initialOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() }
            )
        },
        exitTransition = {
            materialSharedAxisXOut(
                targetOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() }
            )
        },
        popEnterTransition = {
            materialSharedAxisXIn(
                initialOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() }
            )
        },
        popExitTransition = {
            materialSharedAxisXOut(
                targetOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() }
            )
        },
        modifier = modifier
    ) {
        composable(TodoScreen.Main.name) {
            MainPage(
                viewModel = viewModel,
                toSettingsPage = { navController.navigate(TodoScreen.SettingsMain.name) }
            )
        }

        composable(TodoScreen.SettingsMain.name) {
            SettingsMain(
                onNavigateUp = { navController.navigateUp() },
                toAboutPage = { navController.navigate(TodoScreen.SettingsAbout.name) }
            )
        }

        composable(TodoScreen.SettingsAbout.name) {
            SettingsAbout(
                onNavigateUp = { navController.navigateUp() },
                toLicencePage = { navController.navigate(TodoScreen.SettingsAboutLicence.name) }
            )
        }

        composable(TodoScreen.SettingsAboutLicence.name) {
            SettingsAboutLicence(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}