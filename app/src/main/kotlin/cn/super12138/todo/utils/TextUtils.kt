package cn.super12138.todo.utils

import android.text.Editable
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApp

object TextUtils {
    // 更换为id匹配（数据库）
    private val subjectMap = mapOf(
        globalGetString(R.string.subject_chinese) to R.id.subject_chinese,
        globalGetString(R.string.subject_math) to R.id.subject_math,
        globalGetString(R.string.subject_english) to R.id.subject_english,
        globalGetString(R.string.subject_biology) to R.id.subject_biology,
        globalGetString(R.string.subject_geography) to R.id.subject_geography,
        globalGetString(R.string.subject_history) to R.id.subject_history,
        globalGetString(R.string.subject_physics) to R.id.subject_physics,
        globalGetString(R.string.subject_law) to R.id.subject_law,
        globalGetString(R.string.subject_other) to R.id.subject_other
    )

    fun getSubjectName(id: Int): String {
        return when (id) {
            R.id.subject_chinese -> globalGetString(R.string.subject_chinese)
            R.id.subject_math -> globalGetString(R.string.subject_math)
            R.id.subject_english -> globalGetString(R.string.subject_english)
            R.id.subject_biology -> globalGetString(R.string.subject_biology)
            R.id.subject_geography -> globalGetString(R.string.subject_geography)
            R.id.subject_history -> globalGetString(R.string.subject_history)
            R.id.subject_physics -> globalGetString(R.string.subject_physics)
            R.id.subject_law -> globalGetString(R.string.subject_law)
            R.id.subject_other -> globalGetString(R.string.subject_other)
            else -> globalGetString(R.string.subject_unknown)
        }
    }

    fun getSubjectID(name: String): Int? {
        return subjectMap[name]
    }

}

fun globalGetString(resID: Int): String {
    return ToDoApp.context.resources.getString(resID)
}

fun String.toEditable(): Editable? {
    return Editable.Factory.getInstance().newEditable(this)
}