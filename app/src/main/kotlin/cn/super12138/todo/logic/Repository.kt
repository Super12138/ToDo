package cn.super12138.todo.logic

import android.content.Context
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.logic.database.SPHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    /**
     * 设置数据到应用设置中
     */
    fun setPreference(context: Context, key: String, value: Any) =
        SPHelper.setPreference(context, key, value)

    /**
     * 获取应用设置里的数据
     */
    fun getPreferenceString(context: Context, key: String, defaultValue: String) =
        SPHelper.getPreferenceString(context, key, defaultValue)

    /**
     * 获取应用设置的布尔值
     */
    fun getPreferenceBoolean(context: Context, key: String, defaultValue: Boolean) =
        SPHelper.getPreferenceBoolean(context, key, defaultValue)

    // Room
    private val db get() = ToDoApplication.db
    val todoDao = db.toDoRoomDao()

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
    suspend fun getAllUncomplete(): List<ToDoRoom> {
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