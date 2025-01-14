package cn.super12138.todo

import android.app.Application
import cn.super12138.todo.logic.database.TodoDatabase
import cn.super12138.todo.ui.pages.crash.CrashHandler

class TodoApp : Application() {
    private val database by lazy { TodoDatabase.getDatabase(this) }

    companion object {
        lateinit var db: TodoDatabase
    }

    override fun onCreate() {
        super.onCreate()

        db = database

        val crashHandler = CrashHandler(applicationContext)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
    }
}