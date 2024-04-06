package cn.super12138.todo.utils

import android.content.Context
import android.os.Build

object VersionUtils {
    fun getAppVersion(context: Context): String {
        val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val verName = pkgInfo.versionName
        val verCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pkgInfo.longVersionCode.toInt()
        } else {
            pkgInfo.versionCode
        }
        return "$verName($verCode)"
    }
}