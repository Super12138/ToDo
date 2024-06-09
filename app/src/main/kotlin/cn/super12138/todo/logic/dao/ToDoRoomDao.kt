package cn.super12138.todo.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoRoomDao {
    @Insert
    suspend fun insert(toDoRoom: ToDoRoom)

    @Query("SELECT * FROM todo")
    suspend fun getAll(): List<ToDoRoom>

    @Query("SELECT * FROM todo WHERE state = 0")
    suspend fun getAllUnfinished(): List<ToDoRoom>

    @Query("SELECT * FROM todo WHERE state = 1")
    suspend fun getAllComplete(): List<ToDoRoom>

    @Query("DELETE FROM todo")
    suspend fun deleteAll()

    @Query("DELETE FROM todo WHERE uuid = :uuid")
    suspend fun deleteByUUID(uuid: String)

    @Query("UPDATE todo SET state = 1 WHERE uuid = :uuid")
    suspend fun updateStateByUUID(uuid: String)

    @Update
    suspend fun update(toDoRoom: ToDoRoom)
}