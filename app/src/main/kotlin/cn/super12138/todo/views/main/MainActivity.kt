package cn.super12138.todo.views.main
// 2023.11.18立项
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivityMainBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseActivity
import cn.super12138.todo.views.settings.SettingsActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_settings -> {
                    val intent = Intent(ToDoApp.context, SettingsActivity::class.java)
                    startActivity(intent)

                    VibrationUtils.performHapticFeedback(binding.toolBar)

                    true
                }

                else -> false
            }
        }

        // setSupportActionBar(binding.toolbar)
        when (GlobalValues.secureMode) {
            true -> window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )

            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}