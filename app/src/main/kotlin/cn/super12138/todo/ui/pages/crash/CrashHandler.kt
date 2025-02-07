package cn.super12138.todo.ui.pages.crash

import android.content.Context
import android.content.Intent
import android.os.Process
import cn.super12138.todo.ui.activities.CrashActivity
import kotlin.system.exitProcess

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultUEH = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        val stackTrace = ex.stackTraceToString()

        // 启动新的 Activity 来显示崩溃日志
        val intent = Intent(context, CrashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("crash_logs", stackTrace)
        }
        context.startActivity(intent)

        // 传递异常给默认的异常处理器
        defaultUEH?.uncaughtException(thread, ex)

        // 杀掉崩溃的应用程序进程
        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}