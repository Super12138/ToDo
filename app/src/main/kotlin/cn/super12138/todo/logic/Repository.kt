package cn.super12138.todo.logic

import cn.super12138.todo.ToDoApp
import cn.super12138.todo.logic.dao.ToDoRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {

    // Room
    private val db get() = ToDoApp.db
    private val todoDao = db.toDoRoomDao()

    /**
     * @param toDoRoom 要插入的数据
     */
    suspend fun insert(toDoRoom: ToDoRoom) {
        withContext(Dispatchers.IO) {
            todoDao.insert(toDoRoom)
        }
    }

    /**
     * 获取全部未完成的待办
     * @return List<ToDoRoom>
     */
    suspend fun getAllIncomplete(): List<ToDoRoom> {
        return withContext(Dispatchers.IO) {
            todoDao.getAllUnfinished()
        }
    }

    /**
     * 获取全部已完成的待办
     * @return List<ToDoRoom>
     */
    suspend fun getAllComplete(): List<ToDoRoom> {
        return withContext(Dispatchers.IO) {
            todoDao.getAllComplete()
        }
    }

    /**
     * 获取全部待办
     * @return List<ToDoRoom>
     */
    suspend fun getAll(): List<ToDoRoom> {
        return withContext(Dispatchers.IO) {
            todoDao.getAll()
        }
    }

    /**
     * 根据待办的UUID删除指定待办
     * @param uuid 待办的UUID
     */
    suspend fun deleteByUUID(uuid: String) {
        withContext(Dispatchers.IO) {
            todoDao.deleteByUUID(uuid)
        }
    }

    /**
     * 删除全部代办
     */
    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            todoDao.deleteAll()
        }
    }

    /**
     * 根据代办的UUID来把待办状态更新为“已完成”
     * @param uuid 待办的UUID
     */
    suspend fun updateStateByUUID(uuid: String) {
        withContext(Dispatchers.IO) {
            todoDao.updateStateByUUID(uuid)
        }
    }
}