package cn.super12138.todo.logic

import cn.super12138.todo.logic.database.TaskDao
import cn.super12138.todo.logic.database.TaskEntity
import cn.super12138.todo.logic.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow

class Repository(private val taskDao: TaskDao, private val dataStoreManager: DataStoreManager) :
    IRepository {
    override suspend fun insertTask(task: TaskEntity) {
        taskDao.insert(task)
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAll()

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    override suspend fun deleteTaskFromIds(tasks: Set<Int>) {
        taskDao.deleteFromIds(tasks)
    }

    /*override suspend fun deleteAllTodo() {
        toDoDao.deleteAllTodo()
    }*/

    override val dynamicColorFlow = dataStoreManager.dynamicColorFlow
    override val paletteStyleFlow = dataStoreManager.paletteStyleFlow
    override val darkModeFlow = dataStoreManager.darkModeFlow
    override val pureBlackFlow = dataStoreManager.pureBlackFlow
    override val contrastLevelFlow = dataStoreManager.contrastLevelFlow
    override val sortingMethodFlow = dataStoreManager.sortingMethodFlow
    override val textFieldAutoFocusFlow = dataStoreManager.textFieldAutoFocusFlow
    override val secureModeFlow = dataStoreManager.secureModeFlow
    override val hapticFeedbackFlow = dataStoreManager.hapticFeedbackFlow
    override val categoriesFlow = dataStoreManager.categoriesFlow
    override val colorSpecVersionFlow = dataStoreManager.colorSpecVersionFlow
    override val dynamicSchemePlatformFlow = dataStoreManager.dynamicSchemePlatformFlow

    override suspend fun setDynamicColor(value: Boolean) = dataStoreManager.setDynamicColor(value)
    override suspend fun setPaletteStyle(value: Int) = dataStoreManager.setPaletteStyle(value)
    override suspend fun setDarkMode(value: Int) = dataStoreManager.setDarkMode(value)
    override suspend fun setPureBlackMode(value: Boolean) = dataStoreManager.setPureBlackMode(value)
    override suspend fun setContrastLevel(value: Float) = dataStoreManager.setContrastLevel(value)
    override suspend fun setSortingMethod(value: Int) = dataStoreManager.setSortingMethod(value)
    override suspend fun setTextFieldAutoFocus(value: Boolean) =
        dataStoreManager.setTextFieldAutoFocus(value)

    override suspend fun setSecureMode(value: Boolean) = dataStoreManager.setSecureMode(value)
    override suspend fun setHapticFeedback(value: Boolean) =
        dataStoreManager.setHapticFeedback(value)

    override suspend fun setCategories(value: List<String>) = dataStoreManager.setCategories(value)
    override suspend fun setColorSpecVersion(value: Int) =
        dataStoreManager.setColorSpecVersion(value)

    override suspend fun setDynamicSchemePlatform(value: Int) =
        dataStoreManager.setDynamicSchemePlatform(value)
}