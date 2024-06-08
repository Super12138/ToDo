package cn.super12138.todo.views.crash

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import cn.super12138.todo.databinding.ActivityCrashBinding
import cn.super12138.todo.utils.VersionUtils
import cn.super12138.todo.views.BaseActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CrashActivity : BaseActivity<ActivityCrashBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.exitApp) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = if (insets.left == 0) 16 else insets.left
                bottomMargin = if (insets.bottom == 0) 48 else insets.bottom + 32
                rightMargin = if (insets.right == 0) 48 else insets.right + 32
            }
            WindowInsetsCompat.CONSUMED
        }

        val crashLogs = intent.getStringExtra("crash_logs")

        val deviceBrand = Build.BRAND
        val deviceModel = Build.MODEL
        val sdkLevel = Build.VERSION.SDK_INT
        val currentDateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDateTime = formatter.format(currentDateTime)

        val deviceInfo = StringBuilder().apply {
            append("ToDo version: ").append(VersionUtils.getAppVersion(this@CrashActivity))
                .append('\n')
            append("Brand:      ").append("").append(deviceBrand).append('\n')
            append("Model:      ").append(deviceModel).append('\n')
            append("Device SDK: ").append(sdkLevel).append('\n').append('\n')
            append("Crash time: ").append(formattedDateTime).append('\n').append('\n')
            append("======beginning of crash======").append('\n')
        }

        binding.crashLog.text = StringBuilder().apply {
            append(deviceInfo)
            append(crashLogs)
        }

        binding.exitApp.setOnClickListener {
            this.finishAffinity()
        }
    }

    override fun getViewBinding(): ActivityCrashBinding {
        return ActivityCrashBinding.inflate(layoutInflater)
    }
}