package cn.super12138.todo.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import cn.super12138.todo.ui.pages.editor.TodoAddPage
import cn.super12138.todo.ui.pages.editor.TodoEditPage
import cn.super12138.todo.ui.pages.overview.OverviewPage
import cn.super12138.todo.ui.pages.settings.SettingsAbout
import cn.super12138.todo.ui.pages.settings.SettingsAboutLicence
import cn.super12138.todo.ui.pages.settings.SettingsAppearance
import cn.super12138.todo.ui.pages.settings.SettingsData
import cn.super12138.todo.ui.pages.settings.SettingsDataCategory
import cn.super12138.todo.ui.pages.settings.SettingsDeveloperOptions
import cn.super12138.todo.ui.pages.settings.SettingsDeveloperOptionsPadding
import cn.super12138.todo.ui.pages.settings.SettingsInterface
import cn.super12138.todo.ui.pages.settings.SettingsMain
import cn.super12138.todo.ui.pages.tasks.TasksPage
import cn.super12138.todo.ui.theme.fadeThrough
import cn.super12138.todo.ui.theme.materialSharedAxisX
import cn.super12138.todo.ui.viewmodels.MainViewModel


private const val INITIAL_OFFSET_FACTOR = 0.10f

private fun settingsTransition() = NavDisplay.transitionSpec {
    materialSharedAxisX(
        initialOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() },
        targetOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() }
    )
} + NavDisplay.popTransitionSpec {
    materialSharedAxisX(
        initialOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() },
        targetOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() }
    )
} + NavDisplay.predictivePopTransitionSpec {
    materialSharedAxisX(
        initialOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() },
        targetOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopNavigation(
    backStack: TopLevelBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    fun onBack() {
        backStack.removeLast()
    }

//    val veilColor = MaterialTheme.colorScheme.surfaceDim
    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack.backStack,
            onBack = ::onBack,
            /*transitionSpec = {
                fadeIn() togetherWith veilOut(targetColor = veilColor)
            },
            popTransitionSpec = {
                unveilIn(initialColor = veilColor) togetherWith fadeOut()
            },
            predictivePopTransitionSpec = {
                unveilIn(initialColor = veilColor) togetherWith fadeOut()
            },*/
            /**
             * 来自：https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/MaterialFadeThrough.java#L33
             */
            transitionSpec = { fadeThrough() },
            popTransitionSpec = { fadeThrough() },
            predictivePopTransitionSpec = { fadeThrough() },
            entryProvider = entryProvider {
                entry<TodoScreen.Overview> {
                    OverviewPage(viewModel = viewModel)
                }

                entry<TodoScreen.Tasks> {
                    TasksPage(
                        viewModel = viewModel,
                        toTodoAddPage = { backStack.add(TodoScreen.Editor.Add) },
                        toTodoEditPage = { backStack.add(TodoScreen.Editor.Edit(it)) }
                    )
                }

                entry<TodoScreen.Editor.Add> {
                    TodoAddPage(
                        onSave = {
                            viewModel.addTodo(it)
                            onBack()
                        },
                        onNavigateUp = ::onBack
                    )
                }

                entry<TodoScreen.Editor.Edit> { editorArgs ->
                    TodoEditPage(
                        toDo = editorArgs.toDo,
                        onSave = {
                            viewModel.addTodo(it)
                            // 如果原来的待办状态为未完成并且修改后状态为完成
                            if (!editorArgs.toDo.isCompleted && it.isCompleted) {
                                viewModel.playConfetti()
                            }
                            onBack()
                        },
                        onDelete = {
                            viewModel.deleteTodo(editorArgs.toDo)
                            onBack()
                        },
                        onNavigateUp = ::onBack
                    )
                }

                entry<TodoScreen.Settings.Main> {
                    SettingsMain(
                        toAppearancePage = { backStack.add(TodoScreen.Settings.Appearance) },
                        toAboutPage = { backStack.add(TodoScreen.Settings.About) },
                        toInterfacePage = { backStack.add(TodoScreen.Settings.Interface) },
                        toDataPage = { backStack.add(TodoScreen.Settings.Data) },
                    )
                }

                entry<TodoScreen.Settings.Appearance>(metadata = settingsTransition()) {
                    SettingsAppearance(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.Settings.Interface>(metadata = settingsTransition()) {
                    SettingsInterface(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.Settings.Data>(metadata = settingsTransition()) {
                    SettingsData(
                        viewModel = viewModel,
                        toCategoryManager = { backStack.add(TodoScreen.Settings.DataCategory) },
                        onNavigateUp = ::onBack
                    )
                }

                entry<TodoScreen.Settings.DataCategory>(metadata = settingsTransition()) {
                    SettingsDataCategory(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.Settings.About>(metadata = settingsTransition()) {
                    SettingsAbout(
                        //toSpecialPage = { backStack.add(TodoScreen.Settings.AboutSpecial) },
                        toLicencePage = { backStack.add(TodoScreen.Settings.AboutLicence) },
                        toDevPage = { backStack.add(TodoScreen.Settings.DeveloperOptions) },
                        onNavigateUp = ::onBack,
                    )
                }

                /*entry<TodoScreen.Settings.AboutSpecial>(metadata = settingsTransition()) {
                    SettingsAboutSpecial(viewModel = viewModel)
                }*/

                entry<TodoScreen.Settings.AboutLicence>(metadata = settingsTransition()) {
                    SettingsAboutLicence(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.Settings.DeveloperOptions>(metadata = settingsTransition()) {
                    SettingsDeveloperOptions(
                        toPaddingPage = { backStack.add(TodoScreen.Settings.DeveloperOptionsPadding) },
                        onNavigateUp = ::onBack
                    )
                }
                entry<TodoScreen.Settings.DeveloperOptionsPadding>(metadata = settingsTransition()) {
                    SettingsDeveloperOptionsPadding(onNavigateUp = ::onBack)
                }
            },
            modifier = modifier,
        )
    }
}