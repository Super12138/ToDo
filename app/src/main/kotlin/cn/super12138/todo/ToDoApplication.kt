package cn.super12138.todo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cn.super12138.todo.logic.dao.ToDoRoomDB
import cn.super12138.todo.views.crash.CrashHandler
import com.google.android.material.color.DynamicColors

class ToDoApplication : Application() {
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
    }
}