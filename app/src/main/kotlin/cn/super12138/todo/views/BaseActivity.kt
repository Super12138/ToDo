package cn.super12138.todo.views

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import cn.super12138.todo.constant.GlobalValues

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        /*if (GlobalValues.springFestivalTheme) {
            setTheme(R.style.Theme_SpringFestival)
        }*/
        // enableEdgeToEdge(navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        // 确保 Navigation Bar 区域会被显示
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = getViewBinding()
        setContentView(binding.root)

        // 深色模式
        when (GlobalValues.darkMode) {
            "0" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // 适配刘海屏
        val lp = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.attributes = lp
    }

    abstract fun getViewBinding(): T
}