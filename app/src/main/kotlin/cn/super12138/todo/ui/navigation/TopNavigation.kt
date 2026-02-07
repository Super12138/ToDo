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
import cn.super12138.todo.ui.pages.tasks.TasksPage
import cn.super12138.todo.ui.theme.fadeThrough
import cn.super12138.todo.ui.viewmodels.MainViewModel

/**
 * 来自：https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/transition/MaterialFadeThrough.java#L33
 */

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
    /*
         * NavDisplay.transitionSpec {
         *         // Slide new content up, keeping the old content in place underneath
         *         slideInVertically(
         *             initialOffsetY = { it / 5 },
         *             animationSpec = tween(200)
         *         ) + fadeIn(tween(200)) togetherWith ExitTransition.KeepUntilTransitionsFinished
         *     } + NavDisplay.popTransitionSpec {
         *         // Slide old content down, revealing the new content in place underneath
         *         EnterTransition.None togetherWith
         *                 slideOutVertically(
         *                     targetOffsetY = { it / 5 },
         *                     animationSpec = tween(200)
         *                 ) + fadeOut(tween(200))
         *     } + NavDisplay.predictivePopTransitionSpec {
         *         // Slide old content down, revealing the new content in place underneath
         *         EnterTransition.None togetherWith
         *                 slideOutVertically(
         *                     targetOffsetY = { it / 5 },
         *                     animationSpec = tween(200)
         *                 ) + fadeOut(tween(200))
         *     }
         */

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
            transitionSpec = {
                fadeThrough()
            },
            popTransitionSpec = {
                fadeThrough()
            },
            predictivePopTransitionSpec = {
                fadeThrough()
            },
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
                    SettingsNavigation(
                        backStack = viewModel.settingsBackStack,
                        viewModel = viewModel
                    )
                }
            },
            modifier = modifier,
        )
    }
}