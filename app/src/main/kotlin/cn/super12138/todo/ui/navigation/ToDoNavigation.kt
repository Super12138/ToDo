package cn.super12138.todo.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.super12138.todo.ui.pages.main.MainPage
import cn.super12138.todo.ui.viewmodels.MainViewModel

@Composable
fun ToDoNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ToDoScreen.Main.name,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ToDoScreen.Main.name) {
            MainPage(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}