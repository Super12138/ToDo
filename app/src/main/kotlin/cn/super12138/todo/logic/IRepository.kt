package cn.super12138.todo.logic

import cn.super12138.todo.logic.database.TaskEntity
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun insertTask(task: TaskEntity)
    fun getAllTasks(): Flow<List<TaskEntity>>
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun deleteTaskFromIds(tasks: Set<Int>)
    // suspend fun deleteAllTodo()

    val dynamicColorFlow: Flow<Boolean>
    val paletteStyleFlow: Flow<Int>
    val darkModeFlow: Flow<Int>
    val pureBlackFlow: Flow<Boolean>
    val contrastLevelFlow: Flow<Float>
    val sortingMethodFlow: Flow<Int>
    val textFieldAutoFocusFlow: Flow<Boolean>
    val secureModeFlow: Flow<Boolean>
    val hapticFeedbackFlow: Flow<Boolean>
    val categoriesFlow: Flow<List<String>>
    val colorSpecVersionFlow: Flow<Int>
    val dynamicSchemePlatformFlow: Flow<Int>

    suspend fun setDynamicColor(value: Boolean)
    suspend fun setPaletteStyle(value: Int)
    suspend fun setDarkMode(value: Int)
    suspend fun setPureBlackMode(value: Boolean)
    suspend fun setContrastLevel(value: Float)
    suspend fun setSortingMethod(value: Int)
    suspend fun setTextFieldAutoFocus(value: Boolean)
    suspend fun setSecureMode(value: Boolean)
    suspend fun setHapticFeedback(value: Boolean)
    suspend fun setCategories(value: List<String>)
    suspend fun setColorSpecVersion(value: Int)
    suspend fun setDynamicSchemePlatform(value: Int)
}