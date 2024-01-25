package cn.super12138.todo.logic.database

import android.content.Context
import androidx.preference.PreferenceManager

object SPHelper {
    fun getPreferenceString(context: Context, name: String, defaultValue: String): String? {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        return sharedPreferences.getString(name, defaultValue)
    }

    fun getPreferenceBoolean(context: Context, name: String, defaultValue: Boolean): Boolean {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        return sharedPreferences.getBoolean(name, defaultValue)
    }

    fun setPreference(context: Context, name: String, value: Any) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()

        when (value) {
            is String -> editor.putString(name, value)
            is Boolean -> editor.putBoolean(name, value)
            is Int -> editor.putInt(name, value)
            is Float -> editor.putFloat(name, value)
            is Long -> editor.putLong(name, value)
            else -> throw IllegalArgumentException("Unsupported data type")
        }

        editor.apply()
    }
}