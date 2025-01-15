package cn.super12138.todo.logic.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDo: TodoEntity)

    @Query("SELECT * FROM todo")
    fun getAll(): Flow<List<TodoEntity>>

    @Update
    suspend fun update(toDo: TodoEntity)

    @Delete
    suspend fun delete(toDo: TodoEntity)

    @Query("DELETE FROM todo WHERE id in (:toDoIds)")
    suspend fun deleteFromIds(toDoIds: Set<Int>)

    /*@Query("DELETE FROM todo")
    suspend fun deleteAllTodo()*/
}