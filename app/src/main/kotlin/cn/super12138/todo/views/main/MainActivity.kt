package cn.super12138.todo.views.main
// 2023.11.18立项
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivityMainBinding
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.views.BaseActivity
import cn.super12138.todo.views.settings.SettingsActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_settings -> {
                    val intent = Intent(ToDoApp.context, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        // setSupportActionBar(binding.toolbar)
        /*val pref = getSharedPreferences("data", Context.MODE_PRIVATE)
        val isFirstUse = pref.getBoolean("first_use", false)

        if (!isFirstUse) {
            MaterialAlertDialogBuilder(this)
                .setTitle("欢迎使用待办")
                .setMessage("本应用不需要任何权限")
                .setPositiveButton("确定") { dialog, which ->
                    val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("first_use", true)
                    editor.apply()
                }
                .show()
        }*/
        when (GlobalValues.secureMode) {
            true -> window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )

            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}