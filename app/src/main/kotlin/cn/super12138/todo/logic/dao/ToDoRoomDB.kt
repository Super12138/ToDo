package cn.super12138.todo.logic.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoRoom::class], version = 1)
abstract class ToDoRoomDB : RoomDatabase() {
    abstract fun toDoRoomDao(): ToDoRoomDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoRoomDB? = null
        fun getDatabase(context: Context): ToDoRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoRoomDB::class.java,
                    "todo"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}