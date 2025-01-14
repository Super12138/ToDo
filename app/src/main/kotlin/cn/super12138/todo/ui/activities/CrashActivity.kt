package cn.super12138.todo.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import cn.super12138.todo.R
import cn.super12138.todo.ui.pages.crash.CrashPage
import cn.super12138.todo.ui.theme.ToDoTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CrashActivity : ComponentActivity() {
    companion object {
        const val BRAND_PREFIX = "Brand:      "
        const val MODEL_PREFIX = "Model:      "
        const val DEVICE_SDK_PREFIX = "Device SDK: "
        const val CRASH_TIME_PREFIX = "Crash time: "
        const val BEGINNING_CRASH = "======beginning of crash======"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val crashLogs = intent.getStringExtra("crash_logs")

        val deviceBrand = Build.BRAND
        val deviceModel = Build.MODEL
        val sdkLevel = Build.VERSION.SDK_INT
        val currentDateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDateTime = formatter.format(currentDateTime)

        val deviceInfo = StringBuilder().apply {
            append(BRAND_PREFIX).append("").append(deviceBrand).append('\n')
            append(MODEL_PREFIX).append(deviceModel).append('\n')
            append(DEVICE_SDK_PREFIX).append(sdkLevel).append('\n').append('\n')
            append(CRASH_TIME_PREFIX).append(formattedDateTime).append('\n').append('\n')
            append(BEGINNING_CRASH).append('\n')
        }

        val crashLogFormatted = StringBuilder().apply {
            append(deviceInfo)
            append(crashLogs)
        }.toString()

        setContent {
            ToDoTheme {
                val view = LocalView.current
                CrashPage(
                    crashLog = if (crashLogs == null) stringResource(R.string.tip_no_crash_logs) else crashLogFormatted,
                    exitApp = {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        finishAffinity()
                    }
                )
            }
        }
    }
}