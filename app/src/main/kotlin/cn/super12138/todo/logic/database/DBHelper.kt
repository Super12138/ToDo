package cn.super12138.todo.logic.database

import android.annotation.SuppressLint
import android.content.ContentValues
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.logic.model.Progress
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.logic.model.ToDoDatabase

object DBHelper {

    fun insertData(data: ContentValues) {
        ToDoDatabase(
            ToDoApplication.context,
            Constants.DB_NAME,
            Constants.DB_VERSION
        ).writableDatabase.use { db ->
            db.insert(Constants.TABLE_NAME, null, data)
        }
    }

    fun deleteData(delAll: Boolean, uuid: String?) {
        ToDoDatabase(
            ToDoApplication.context,
            Constants.DB_NAME,
            Constants.DB_VERSION
        ).writableDatabase.use { db ->
            if (delAll) {
                db.delete(Constants.TABLE_NAME, null, null)
            } else {
                if (uuid == null) {
                    throw RuntimeException("uuid cannot be null")
                }
                db.delete(Constants.TABLE_NAME, "uuid = ?", arrayOf(uuid))
            }
        }
    }

    fun updateData(uuid: String, newData: ContentValues) {
        val dbHelper = ToDoDatabase(
            ToDoApplication.context,
            Constants.DB_NAME,
            Constants.DB_VERSION
        ).writableDatabase
        dbHelper.update(Constants.TABLE_NAME, newData, "uuid = ?", arrayOf(uuid))
    }

    @SuppressLint("Range")
    fun getCompleteTotalCount(): Progress {
        var total = 0
        var complete = 0

        ToDoDatabase(
            ToDoApplication.context,
            Constants.DB_NAME,
            Constants.DB_VERSION
        ).writableDatabase.use { db ->
            db.query(Constants.TABLE_NAME, null, null, null, null, null, null, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val state = cursor.getString(cursor.getColumnIndex("state"))
                        if (state.toInt() == 1) {
                            complete += 1
                        }
                        total += 1
                    } while (cursor.moveToNext())
                    cursor.close()
                }
            }
        }

        return Progress(complete, total)
    }

    @SuppressLint("Range")
    fun getAllData(): MutableList<ToDo> {
        val todoList = mutableListOf<ToDo>()
        ToDoDatabase(
            ToDoApplication.context,
            Constants.DB_NAME,
            Constants.DB_VERSION
        ).writableDatabase.query(
            Constants.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                    val subject = cursor.getString(cursor.getColumnIndex("subject"))
                    val state = cursor.getString(cursor.getColumnIndex("state"))
                    val todoContext = cursor.getString(cursor.getColumnIndex("context"))

                    if (state.toInt() != 1) {
                        todoList.add(ToDo(uuid, todoContext, subject))
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return todoList
    }
}