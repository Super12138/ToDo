package cn.super12138.todo.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import cn.super12138.todo.ui.VerveDoDefaults

fun LazyListScope.bottomPadding() {
    item {
        Spacer(Modifier.size(VerveDoDefaults.screenHorizontalPadding))
    }
    item {
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}