package cn.super12138.todo.views.main
// 2023.11.18立项
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.databinding.ActivityMainBinding
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.views.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // 适配刘海屏
        val lp = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.attributes = lp

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        /*val pref = getSharedPreferences("data",Context.MODE_PRIVATE)
        val isFirstUse = pref.getBoolean("first_use", false)

        if (!isFirstUse) {
            MaterialAlertDialogBuilder(this)
                .setTitle("欢迎使用待办")
                .setMessage("")
                .setPositiveButton("确定") { dialog, which ->
                    val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("first_use", true)
                    editor.apply()
                }
                .show()
        }
*/
        val isDarkMode = Repository.getPreferenceString(this, "dark_mode", "0")
        when (isDarkMode) {
            "0" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_settings -> {
                val intent = Intent(ToDoApplication.context, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}