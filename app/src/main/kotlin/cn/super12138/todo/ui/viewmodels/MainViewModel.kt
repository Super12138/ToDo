package cn.super12138.todo.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.TodoApp
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.constants.GlobalValues
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.ContrastLevel
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.SortingMethod
import cn.super12138.todo.ui.theme.PaletteStyle
import cn.super12138.todo.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class MainViewModel : ViewModel() {
    // 待办
    private val toDos: Flow<List<TodoEntity>> = Repository.getAllTodos()
    var appSortingMethod by mutableStateOf(SortingMethod.fromId(GlobalValues.sortingMethod))
    val sortedTodos: Flow<List<TodoEntity>> = toDos.map { list ->
        when (appSortingMethod) {
            SortingMethod.Sequential -> list.sortedBy { it.id }
            SortingMethod.Subject -> list.sortedBy { it.subject }
            SortingMethod.Priority -> list.sortedByDescending { it.priority } // 优先级高的在前
            SortingMethod.Completion -> list.sortedBy { it.isCompleted } // 未完成的在前
            SortingMethod.AlphabeticalAscending -> list.sortedBy { it.content }
            SortingMethod.AlphabeticalDescending -> list.sortedByDescending { it.content }
        }
    }

    val showConfetti = mutableStateOf(false)
    var selectedEditTodo by mutableStateOf<TodoEntity?>(null)
        private set
    var showCompletedTodos by mutableStateOf(GlobalValues.showCompleted)

    // 主题颜色
    var appDynamicColorEnable by mutableStateOf(GlobalValues.dynamicColor)
        private set
    var appPaletteStyle by mutableStateOf(PaletteStyle.fromId(GlobalValues.paletteStyle))
        private set
    var appDarkMode by mutableStateOf(DarkMode.fromId(GlobalValues.darkMode))
        private set
    var appContrastLevel by mutableStateOf(ContrastLevel.fromFloat(GlobalValues.contrastLevel))
        private set
    var appSecureMode by mutableStateOf(GlobalValues.secureMode)
        private set

    // 多选逻辑参考：https://github.com/X1nto/Mauth
    private val _selectedTodoIds = MutableStateFlow(listOf<Int>())
    val selectedTodoIds = _selectedTodoIds.asStateFlow()

    fun addTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.insertTodo(toDo)
        }
    }

    fun updateTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.updateTodo(toDo)
        }
    }

    fun deleteTodo(toDo: TodoEntity) {
        viewModelScope.launch {
            Repository.deleteTodo(toDo)
        }
    }

    /*fun deleteAllTodo() {
        viewModelScope.launch {
            Repository.deleteAllTodo()
        }
    }*/

    fun setEditTodoItem(toDo: TodoEntity?) {
        selectedEditTodo = toDo
    }

    /**
     * 切换待办的选择状态
     */
    fun toggleTodoSelection(toDo: TodoEntity) {
        _selectedTodoIds.update { idList ->
            if (idList.contains(toDo.id)) {
                // 若已经选择取消选择
                idList - toDo.id
            } else {
                // 若未选择添加到列表中，立即选中
                idList + toDo.id
            }
        }
    }

    /**
     * 切换是否全选
     */
    fun selectAllTodos() {
        viewModelScope.launch {
            toDos.firstOrNull()?.let { todos ->
                // 无论是否有选择都全选
                val allIds = todos.map { it.id }
                _selectedTodoIds.value = allIds
            }
        }
    }

    /**
     * 清除全部已选择的待办
     */
    fun clearAllTodoSelection() {
        _selectedTodoIds.update { emptyList() }
    }

    /**
     * 删除选择的待办
     */
    fun deleteSelectedTodo() {
        viewModelScope.launch {
            Repository.deleteTodoFromIds(selectedTodoIds.value)
            clearAllTodoSelection()
        }
    }

    fun playConfetti() {
        showConfetti.value = true
    }

    /**
     * 应用设置
     */
    fun setDynamicColor(enabled: Boolean) {
        appDynamicColorEnable = enabled
    }

    fun setPaletteStyle(paletteStyle: PaletteStyle) {
        appPaletteStyle = paletteStyle
    }

    fun setDarkMode(darkMode: DarkMode) {
        appDarkMode = darkMode
    }

    fun setContrastLevel(contrastLevel: ContrastLevel) {
        appContrastLevel = contrastLevel
    }

    fun setShowCompleted(show: Boolean) {
        showCompletedTodos = show
    }

    fun setSortingMethod(sortingMethod: SortingMethod) {
        appSortingMethod = sortingMethod
    }

    fun setSecureMode(enabled: Boolean) {
        appSecureMode = enabled
    }

    fun backupAppData(uri: Uri, context: Context, onResult: (completed: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    ZipOutputStream(BufferedOutputStream(outputStream)).use { zipOutStream ->
                        getBackupFiles(context).forEach { file ->
                            FileUtils.addFileToZip(file, file.name, zipOutStream)
                        }
                    }
                }
            }.isSuccess
            withContext(Dispatchers.Main) { onResult(result) }
        }
    }

    fun restoreAppData(uri: Uri, context: Context, onResult: (completed: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = runCatching {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    ZipInputStream(BufferedInputStream(inputStream)).use { zipInputStream ->
                        extractZipEntries(zipInputStream, context)
                    }
                }
            }.isSuccess
            withContext(Dispatchers.Main) { onResult(result) }
        }
    }

    private fun getBackupFiles(context: Context): List<File> {
        val dbPath = TodoApp.db.openHelper.writableDatabase.path
        val prefPath = "${context.filesDir.parent}/shared_prefs"
        return listOf(
            context.getDatabasePath(Constants.DB_NAME), // 数据库
            File("$dbPath-wal"), // 数据库-wal
            File("$dbPath-shm"), // 数据库-shm
            File("$prefPath/${Constants.SP_NAME}.xml") // SharedPreferences
        ).filter { it.exists() }
    }

    private fun extractZipEntries(zipInputStream: ZipInputStream, context: Context) {
        val dbPath = context.getDatabasePath(Constants.DB_NAME).parent
        val prefPath = "${context.filesDir.parent}/shared_prefs/"
        generateSequence { zipInputStream.nextEntry }.forEach { zipEntry ->
            val outputFile = File(
                if (zipEntry.name.endsWith(".xml")) prefPath else dbPath,
                zipEntry.name
            )
            if (zipEntry.isDirectory) {
                outputFile.mkdirs()
            } else {
                outputFile.parentFile?.mkdirs()
                FileOutputStream(outputFile).use { zipInputStream.copyTo(it) }
            }
            zipInputStream.closeEntry()
        }
    }
}