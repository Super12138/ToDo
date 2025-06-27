package cn.super12138.todo.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import cn.super12138.todo.ui.pages.settings.SettingsData
import cn.super12138.todo.ui.pages.settings.SettingsInterface
import cn.super12138.todo.ui.pages.settings.SettingsMain
import cn.super12138.todo.ui.theme.materialSharedAxisXIn
import cn.super12138.todo.ui.theme.materialSharedAxisXOut
import cn.super12138.todo.ui.viewmodels.MainViewModel

private const val INITIAL_OFFSET_FACTOR = 0.10f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TodoNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TodoScreen.Main.name,
    viewModel: MainViewModel
) {
    SharedTransitionLayout {
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
                    toSettingsPage = { navController.navigate(TodoScreen.SettingsMain.name) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(TodoScreen.TodoEditor.name) {
                TodoEditorPage(
                    toDo = viewModel.selectedEditTodo,
                    onSave = {
                        viewModel.addTodo(it)
                        // 如果原来的待办状态为未完成并且修改后状态为完成
                        if (viewModel.selectedEditTodo?.isCompleted != true && it.isCompleted) {
                            viewModel.playConfetti()
                        }
                        navController.navigateUp()
                    },
                    onDelete = {
                        if (viewModel.selectedEditTodo !== null) {
                            viewModel.deleteTodo(viewModel.selectedEditTodo!!)
                            viewModel.setEditTodoItem(null)
                        }
                        navController.navigateUp()
                    },
                    onNavigateUp = { navController.navigateUp() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }

            composable(TodoScreen.SettingsMain.name) {
                SettingsMain(
                    toAppearancePage = { navController.navigate(TodoScreen.SettingsAppearance.name) },
                    toAboutPage = { navController.navigate(TodoScreen.SettingsAbout.name) },
                    toInterfacePage = { navController.navigate(TodoScreen.SettingsInterface.name) },
                    toDataPage = { navController.navigate(TodoScreen.SettingsData.name) },
                    onNavigateUp = { navController.navigateUp() },
                )
            }

            composable(TodoScreen.SettingsAppearance.name) {
                SettingsAppearance(onNavigateUp = { navController.navigateUp() })
            }

            composable(TodoScreen.SettingsInterface.name) {
                SettingsInterface(onNavigateUp = { navController.navigateUp() })
            }

            composable(TodoScreen.SettingsData.name) {
                SettingsData(
                    viewModel = viewModel,
                    onNavigateUp = { navController.navigateUp() }
                )
            }

            composable(TodoScreen.SettingsAbout.name) {
                SettingsAbout(
                    //toSpecialPage = { navController.navigate(TodoScreen.SettingsAboutSpecial.name) },
                    toLicencePage = { navController.navigate(TodoScreen.SettingsAboutLicence.name) },
                    onNavigateUp = { navController.navigateUp() },
                )
            }

            /*composable(TodoScreen.SettingsAboutSpecial.name) {
                SettingsAboutSpecial(viewModel = viewModel)
            }*/

            composable(TodoScreen.SettingsAboutLicence.name) {
                SettingsAboutLicence(onNavigateUp = { navController.navigateUp() })
            }
        }
    }
}