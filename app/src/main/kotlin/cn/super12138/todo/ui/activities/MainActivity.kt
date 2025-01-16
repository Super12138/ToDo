package cn.super12138.todo.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.super12138.todo.ui.components.Konfetti
import cn.super12138.todo.ui.navigation.TodoNavigation
import cn.super12138.todo.ui.theme.ToDoTheme
import cn.super12138.todo.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val mainViewModel: MainViewModel = viewModel()
                    val showConfetti = mainViewModel.showConfetti
                    TodoNavigation(viewModel = mainViewModel)
                    Konfetti(state = showConfetti)
                }
            }
        }
    }
}