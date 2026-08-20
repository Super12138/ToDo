package cn.super12138.todo.logic

import cn.super12138.todo.logic.database.TaskDao
import cn.super12138.todo.logic.database.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insert(task)
    }

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAll()

    suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    suspend fun deleteTaskFromIds(tasks: Set<Int>) {
        taskDao.deleteFromIds(tasks)
    }

    /*suspend fun deleteAllTodo() {
        toDoDao.deleteAllTodo()
    }*/
}