package cn.super12138.todo.utils

import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object SystemUtils {
    /**
     * 获取应用版本号
     * @return 版本名称（版本代码）
     */
    fun getAppVersion(context: Context): String {
        val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val verName = pkgInfo.versionName
        val verCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pkgInfo.longVersionCode.toInt()
        } else {
            pkgInfo.versionCode
        }
        return "$verName ($verCode)"
    }

    /**
     * 获取格式化后的当前时间
     * 参考 https://github.com/rafi0101/Android-Room-Database-Backup/blob/master/core/src/main/java/de/raphaelebner/roomdatabasebackup/core/RoomBackup.kt#L770
     * @return 当前时间
     */
    fun getTime(): String {
        val currentTime = Calendar.getInstance().time

        val sdf =
            if (Build.VERSION.SDK_INT <= 28) {
                SimpleDateFormat("yyyy-MM-dd-HH_mm_ss", Locale.getDefault())
            } else {
                SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault())
            }

        return sdf.format(currentTime)
    }
}