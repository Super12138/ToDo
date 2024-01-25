package cn.super12138.todo.views

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.logic.Repository

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val springFestivalTheme =
            Repository.getPreferenceBoolean(ToDoApplication.context, "spring_festival_theme", false)
        if (springFestivalTheme) {
            setTheme(R.style.Theme_SpringFestival)
        }
        // enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // 适配刘海屏
        val lp = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.attributes = lp
    }
}