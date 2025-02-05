package cn.super12138.todo.ui.pages.settings.state

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * 来自：https://github.com/hushenghao/AndroidEasterEggs/blob/main/app/src/main/java/com/dede/android_eggs/views/settings/compose/basic/PrefMutableState.kt
 * 在命名上有改动
 * @author hushenghao
 */

@Composable
fun rememberPrefBooleanState(key: String, default: Boolean): MutableState<Boolean> {
    val context = LocalContext.current
    return remember { PrefMutableBooleanState(context, key, default) }
}

@Composable
fun rememberPrefIntState(key: String, default: Int): MutableIntState {
    val context = LocalContext.current
    return remember { PrefMutableIntState(context, key, default) }
}

private class PrefMutableBooleanState(
    val context: Context,
    val key: String,
    default: Boolean,
) : MutableState<Boolean> {
    private val delegate = mutableStateOf(context.pref.getBoolean(key, default))

    override var value: Boolean
        get() = delegate.value
        set(value) {
            delegate.value = value
            context.pref.edit().putBoolean(key, value).apply()
        }

    override fun component1(): Boolean {
        return delegate.component1()
    }

    override fun component2(): (Boolean) -> Unit {
        return delegate.component2()
    }
}

private class PrefMutableIntState(
    val context: Context,
    val key: String,
    default: Int,
) : MutableIntState {
    private val delegate = mutableIntStateOf(context.pref.getInt(key, default))

    override var intValue: Int
        get() = delegate.intValue
        set(value) {
            delegate.intValue = value
            context.pref.edit().putInt(key, value).apply()
        }

    override fun component1(): Int {
        return delegate.component1()
    }

    override fun component2(): (Int) -> Unit {
        return delegate.component2()
    }
}

/**
 * 来自：https://github.com/hushenghao/AndroidEasterEggs/blob/main/basic/src/main/java/com/dede/android_eggs/util/Pref.kt
 * @author hushenghao
 */
val Context.pref: SharedPreferences
    get() {
        return applicationContext.getSharedPreferences(
            applicationContext.packageName + "_preferences",
            Context.MODE_PRIVATE
        )
    }