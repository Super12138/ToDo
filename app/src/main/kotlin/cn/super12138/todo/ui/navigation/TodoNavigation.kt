package cn.super12138.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.super12138.todo.ui.pages.editor.TodoEditorPage
import cn.super12138.todo.ui.pages.main.MainPage
import cn.super12138.todo.ui.pages.settings.SettingsAbout
import cn.super12138.todo.ui.pages.settings.SettingsAboutLicence
import cn.super12138.todo.ui.pages.settings.SettingsAppearance
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
                toTodoEditPage = { navController.navigate(TodoScreen.TodoEditor.name) },
                toSettingsPage = { navController.navigate(TodoScreen.SettingsMain.name) }
            )
        }

        composable(TodoScreen.TodoEditor.name) {
            TodoEditorPage(
                toDo = viewModel.selectedEditTodo,
                onSave = {
                    viewModel.addTodo(it)
                    viewModel.setEditTodoItem(null)
                    navController.navigateUp()
                },
                onDelete = {
                    if (viewModel.selectedEditTodo !== null) {
                        viewModel.deleteTodo(viewModel.selectedEditTodo!!)
                        viewModel.setEditTodoItem(null)
                    }
                    navController.navigateUp()
                },
                onNavigateUp = {
                    navController.navigateUp()
                    viewModel.setEditTodoItem(null)
                }
            )
        }

        composable(TodoScreen.SettingsMain.name) {
            SettingsMain(
                onNavigateUp = { navController.navigateUp() },
                toAppearancePage = { navController.navigate(TodoScreen.SettingsAppearance.name) },
                toAboutPage = { navController.navigate(TodoScreen.SettingsAbout.name) }
            )
        }

        composable(TodoScreen.SettingsAppearance.name) {
            SettingsAppearance(
                viewModel = viewModel,
                onNavigateUp = { navController.navigateUp() }
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