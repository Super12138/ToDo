package cn.super12138.todo.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.super12138.todo.ui.pages.editor.TodoEditorPage
import cn.super12138.todo.ui.pages.main.MainPage
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
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey> = rememberNavBackStack(TodoScreen.Main),
    viewModel: MainViewModel
) {
    fun onBack() {
        backStack.removeAt(backStack.lastIndex)
    }

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
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
                entry<TodoScreen.Main> {
                    MainPage(
                        viewModel = viewModel,
                        toTodoEditPage = { backStack.add(TodoScreen.Editor(it)) },
                        toSettingsPage = { backStack.add(TodoScreen.SettingsMain) },
                        sharedTransitionScope = this@SharedTransitionLayout
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

                entry<TodoScreen.SettingsMain> {
                    SettingsMain(
                        toAppearancePage = { backStack.add(TodoScreen.SettingsAppearance) },
                        toAboutPage = { backStack.add(TodoScreen.SettingsAbout) },
                        toInterfacePage = { backStack.add(TodoScreen.SettingsInterface) },
                        toDataPage = { backStack.add(TodoScreen.SettingsData) },
                        onNavigateUp = ::onBack,
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