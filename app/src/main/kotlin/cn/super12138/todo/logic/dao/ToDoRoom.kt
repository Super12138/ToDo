package cn.super12138.todo.logic.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param uuid String 待办的uuid
 * @param state Int 待办的完成状态，0表示未完成，1表示完成
 * @param subject String 待办的学科
 * @param context String 待办的内容
 */
@Entity(tableName = "todo")
data class ToDoRoom(
    @PrimaryKey @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "context") val context: String
)