package cn.super12138.todo.logic

import cn.super12138.todo.TodoApp
import cn.super12138.todo.logic.database.TodoEntity
import kotlinx.coroutines.flow.Flow

object Repository {
    private val db get() = TodoApp.db
    private val toDoDao = db.toDoDao()

    suspend fun insertTodo(toDo: TodoEntity) {
        toDoDao.insert(toDo)
    }

    fun getAllTodos(): Flow<List<TodoEntity>> = toDoDao.getAll()

    suspend fun updateTodo(toDo: TodoEntity) {
        toDoDao.update(toDo)
    }

    suspend fun deleteTodo(toDo: TodoEntity) {
        toDoDao.delete(toDo)
    }

    suspend fun deleteTodoFromIds(toDoItems: List<Int>) {
        toDoDao.deleteFromIds(toDoItems.toSet())
    }

    /*suspend fun deleteAllTodo() {
        toDoDao.deleteAllTodo()
    }*/
}