package cn.super12138.todo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.logic.dao.ToDoRoomDB
import cn.super12138.todo.views.crash.CrashHandler
import cn.super12138.todo.views.welcome.WelcomeActivity
import com.google.android.material.color.DynamicColors

class ToDoApp : Application() {
    private val database by lazy { ToDoRoomDB.getDatabase(this) }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var db: ToDoRoomDB
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        context = applicationContext

        val crashHandler = CrashHandler(this)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)

        db = database

        if (!GlobalValues.welcomePage) {
            val intent = Intent(this, WelcomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}