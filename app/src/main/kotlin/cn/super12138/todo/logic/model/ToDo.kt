package cn.super12138.todo.logic.model

data class ToDo(val uuid: String, val context: String, val subject: String)

/*
package cn.super12138.todo.logic.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class ToDo (
    @PrimaryKey @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "context") val context: String
)
 */