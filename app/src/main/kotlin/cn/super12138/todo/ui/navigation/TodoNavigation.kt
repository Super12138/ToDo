package cn.super12138.todo.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import cn.super12138.todo.ui.pages.editor.TodoEditorPage
import cn.super12138.todo.ui.pages.main.MainPage
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
import cn.super12138.todo.ui.theme.materialSharedAxisX
import cn.super12138.todo.ui.viewmodels.MainViewModel

private const val INITIAL_OFFSET_FACTOR = 0.10f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TodoNavigation(
    backStack: TopLevelBackStack<NavKey>,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    fun onBack() {
        backStack.removeLast()
    }

    val anim = NavDisplay.transitionSpec {
        // Slide new content up, keeping the old content in place underneath
        slideInVertically(
            initialOffsetY = { it / 5 },
            animationSpec = tween(200)
        ) + fadeIn(tween(200)) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it / 5 },
                    animationSpec = tween(200)
                ) + fadeOut(tween(200))
    } + NavDisplay.predictivePopTransitionSpec {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it / 5 },
                    animationSpec = tween(200)
                ) + fadeOut(tween(200))
    }

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack.backStack,
            onBack = ::onBack,
            transitionSpec = {
                materialSharedAxisX(
                    initialOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() },
                    targetOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() })
            },
            popTransitionSpec = {
                materialSharedAxisX(
                    initialOffsetX = { -(it * INITIAL_OFFSET_FACTOR).toInt() },
                    targetOffsetX = { (it * INITIAL_OFFSET_FACTOR).toInt() })
            },
            entryProvider = entryProvider {
                entry<TodoScreen.Overview>(metadata = anim) {
                    OverviewPage()
                }
                entry<TodoScreen.Tasks>(metadata = anim) {
                    MainPage(
                        viewModel = viewModel,
                        toTodoEditPage = { backStack.add(TodoScreen.Editor(it)) },
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }

                entry<TodoScreen.Settings>(metadata = anim) {
                    SettingsMain(
                        toAppearancePage = { backStack.add(TodoScreen.SettingsAppearance) },
                        toAboutPage = { backStack.add(TodoScreen.SettingsAbout) },
                        toInterfacePage = { backStack.add(TodoScreen.SettingsInterface) },
                        toDataPage = { backStack.add(TodoScreen.SettingsData) },
                    )
                }

                entry<TodoScreen.Editor> { editorArgs ->
                    TodoEditorPage(
                        toDo = editorArgs.toDo,
                        onSave = {
                            viewModel.addTodo(it)
                            // 如果原来的待办状态为未完成并且修改后状态为完成
                            if (editorArgs.toDo?.isCompleted != true && it.isCompleted) {
                                viewModel.playConfetti()
                            }
                            onBack()
                        },
                        onDelete = {
                            if (editorArgs.toDo !== null) {
                                viewModel.deleteTodo(editorArgs.toDo)
                            }
                            onBack()
                        },
                        onNavigateUp = ::onBack,
                        sharedTransitionScope = this@SharedTransitionLayout
                    )
                }

                entry<TodoScreen.SettingsAppearance> {
                    SettingsAppearance(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.SettingsInterface> {
                    SettingsInterface(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.SettingsData> {
                    SettingsData(
                        viewModel = viewModel,
                        toCategoryManager = { backStack.add(TodoScreen.SettingsDataCategory) },
                        onNavigateUp = ::onBack
                    )
                }

                entry<TodoScreen.SettingsDataCategory> {
                    SettingsDataCategory(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.SettingsAbout> {
                    SettingsAbout(
                        //toSpecialPage = { backStack.add(TodoScreen.SettingsAboutSpecial) },
                        toLicencePage = { backStack.add(TodoScreen.SettingsAboutLicence) },
                        toDevPage = { backStack.add(TodoScreen.SettingsDev) },
                        onNavigateUp = ::onBack,
                    )
                }

                /*entry<TodoScreen.SettingsAboutSpecial> {
                    SettingsAboutSpecial(viewModel = viewModel)
                }*/

                entry<TodoScreen.SettingsAboutLicence> {
                    SettingsAboutLicence(onNavigateUp = ::onBack)
                }

                entry<TodoScreen.SettingsDev> {
                    SettingsDeveloperOptions(
                        toPaddingPage = { backStack.add(TodoScreen.SettingsDevPadding) },
                        onNavigateUp = ::onBack
                    )
                }
                entry<TodoScreen.SettingsDevPadding> {
                    SettingsDeveloperOptionsPadding(onNavigateUp = ::onBack)
                }
            },
            modifier = modifier,
        )
    }
}