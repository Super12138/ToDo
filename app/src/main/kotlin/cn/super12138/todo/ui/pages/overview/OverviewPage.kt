package cn.super12138.todo.ui.pages.overview

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.components.TopAppBarScaffold

@Composable
fun OverviewPage(modifier: Modifier = Modifier) {
    TopAppBarScaffold(
        title = stringResource(R.string.page_overview),
        modifier = modifier
    ) {
        Row {
            Card {
                Icon(painter = painterResource(R.drawable.ic_ballot), contentDescription = null)
                Text("总任务")
            }
        }
    }
}