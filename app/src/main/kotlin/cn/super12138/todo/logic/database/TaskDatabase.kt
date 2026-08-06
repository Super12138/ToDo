package cn.super12138.todo.logic.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import cn.super12138.todo.constants.Constants

@Database(entities = [TaskEntity::class], version = 5)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override suspend fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE ${Constants.DB_TABLE_NAME} ADD COLUMN custom_subject TEXT NOT NULL DEFAULT ''")
            }
        }

        // 为自定义学科功能进行迁移
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override suspend fun migrate(connection: SQLiteConnection) {
                // 创建一个新表，其中不含有subject，并且有一个新的category字段（由custom_subject迁移而来）
                connection.execSQL("CREATE TABLE IF NOT EXISTS todo_new (content TEXT NOT NULL, category TEXT NOT NULL DEFAULT '', completed INTEGER NOT NULL, priority REAL NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
                // 将旧表中的数据迁移到新表中
                connection.execSQL("INSERT INTO todo_new (content, category, completed, priority, id) SELECT content, COALESCE(NULLIF(custom_subject, ''), '') AS category, completed, priority, id FROM todo")
                // 删除旧表
                connection.execSQL("DROP TABLE todo")
                // 重命名新表
                connection.execSQL("ALTER TABLE todo_new RENAME TO todo")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override suspend fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE ${Constants.DB_TABLE_NAME} ADD COLUMN due_date INTEGER")
            }
        }
    }
}