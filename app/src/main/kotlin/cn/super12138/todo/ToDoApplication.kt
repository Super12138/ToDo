package cn.super12138.todo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cn.super12138.todo.views.crash.CrashHandler
import com.google.android.material.color.DynamicColors

class ToDoApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        context = applicationContext

        val crashHandler = CrashHandler(this)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
    }
}