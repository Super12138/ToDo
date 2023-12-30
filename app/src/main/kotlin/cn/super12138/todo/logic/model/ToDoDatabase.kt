package cn.super12138.todo.logic.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ToDoDatabase(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    /*
     * @param uuid: String 待办的uuid
     * @param state: Int 待办的完成状态，0表示未完成，1表示完成
     * @param subject: String 待办的学科
     * @param context: String 待办的内容
     */
    private val createToDo = "create table ToDo (" +
            "uuid text primary key," +
            "state integer," +
            "subject text," +
            "context text)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createToDo)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
