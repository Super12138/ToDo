package cn.super12138.todo.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.super12138.todo.constants.Constants

@Database(entities = [TodoEntity::class], version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun toDoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null
        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    Constants.DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}