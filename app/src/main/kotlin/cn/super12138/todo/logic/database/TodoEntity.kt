package cn.super12138.todo.logic.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class TodoEntity(
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "completed") val isCompleted: Boolean = false,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0L,
)
