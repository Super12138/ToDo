package cn.super12138.todo.logic

import cn.super12138.todo.logic.database.TodoEntity
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun insertTodo(toDo: TodoEntity)

    fun getAllTodos(): Flow<List<TodoEntity>>

    suspend fun updateTodo(toDo: TodoEntity)

    suspend fun deleteTodo(toDo: TodoEntity)

    suspend fun deleteTodoFromIds(toDoItems: List<Int>)

    // suspend fun deleteAllTodo()
}