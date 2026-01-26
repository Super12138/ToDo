package cn.super12138.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
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

@Composable
fun SettingsNavigation(
    backStack: NavBackStack<NavKey>,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    fun onBack() {
        backStack.removeLastOrNull()
    }

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
            entry<TodoScreen.Settings.Main> {
                SettingsMain(
                    toAppearancePage = { backStack.add(TodoScreen.Settings.Appearance) },
                    toAboutPage = { backStack.add(TodoScreen.Settings.About) },
                    toInterfacePage = { backStack.add(TodoScreen.Settings.Interface) },
                    toDataPage = { backStack.add(TodoScreen.Settings.Data) },
                )
            }

            entry<TodoScreen.Settings.Appearance> {
                SettingsAppearance(onNavigateUp = ::onBack)
            }

            entry<TodoScreen.Settings.Interface> {
                SettingsInterface(onNavigateUp = ::onBack)
            }

            entry<TodoScreen.Settings.Data> {
                SettingsData(
                    viewModel = viewModel,
                    toCategoryManager = { backStack.add(TodoScreen.Settings.DataCategory) },
                    onNavigateUp = ::onBack
                )
            }

            entry<TodoScreen.Settings.DataCategory> {
                SettingsDataCategory(onNavigateUp = ::onBack)
            }

            entry<TodoScreen.Settings.About> {
                SettingsAbout(
                    //toSpecialPage = { backStack.add(TodoScreen.Settings.AboutSpecial) },
                    toLicencePage = { backStack.add(TodoScreen.Settings.AboutLicence) },
                    toDevPage = { backStack.add(TodoScreen.Settings.DeveloperOptions) },
                    onNavigateUp = ::onBack,
                )
            }

            /*entry<TodoScreen.Settings.AboutSpecial> {
                SettingsAboutSpecial(viewModel = viewModel)
            }*/

            entry<TodoScreen.Settings.AboutLicence> {
                SettingsAboutLicence(onNavigateUp = ::onBack)
            }

            entry<TodoScreen.Settings.DeveloperOptions> {
                SettingsDeveloperOptions(
                    toPaddingPage = { backStack.add(TodoScreen.Settings.DeveloperOptionsPadding) },
                    onNavigateUp = ::onBack
                )
            }
            entry<TodoScreen.Settings.DeveloperOptionsPadding> {
                SettingsDeveloperOptionsPadding(onNavigateUp = ::onBack)
            }
        },
        modifier = modifier,
    )
}