package cn.super12138.todo.logic

import android.content.ContentValues
import android.content.Context
import cn.super12138.todo.logic.database.DBHelper
import cn.super12138.todo.logic.database.SPHelper

object Repository {
    fun getCompleteTotalCount() = DBHelper.getCompleteTotalCount()

    fun insertData(data: ContentValues) = DBHelper.insertData(data)

    fun deleteData(deleteAll: Boolean, uuid: String?) = DBHelper.deleteData(deleteAll, uuid)

    fun updateData(uuid: String, newData: ContentValues) = DBHelper.updateData(uuid, newData)

    fun getAllData() = DBHelper.getAllData()

    fun getPreferenceString(context: Context, key: String, defaultValue: String) = SPHelper.getPreferenceString(context, key, defaultValue)
}