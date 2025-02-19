package cn.super12138.todo.logic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.super12138.todo.constants.Constants

@Entity(tableName = Constants.DB_TABLE_NAME)
data class TodoEntity(
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "subject") val subject: Int,
    @ColumnInfo(name = "custom_subject") val customSubject: String = "",
    @ColumnInfo(name = "completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "priority") val priority: Float,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
)
