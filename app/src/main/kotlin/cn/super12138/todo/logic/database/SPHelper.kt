package cn.super12138.todo.logic.database

import android.content.Context
import androidx.preference.PreferenceManager

object SPHelper {
    fun getPreferenceString(context: Context, name: String, defaultValue: String): String? {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        return sharedPreferences.getString(name, defaultValue)
    }
    fun getPreferenceBoolean(context: Context, name: String, defaultValue: Boolean): Boolean? {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        return sharedPreferences.getBoolean(name, defaultValue)
    }
}