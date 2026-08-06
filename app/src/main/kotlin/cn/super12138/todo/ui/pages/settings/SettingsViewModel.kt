package cn.super12138.todo.ui.pages.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.constants.Constants
import cn.super12138.todo.logic.IRepository
import cn.super12138.todo.logic.model.ColorSpecVersion
import cn.super12138.todo.logic.model.ContrastLevel
import cn.super12138.todo.logic.model.DarkMode
import cn.super12138.todo.logic.model.DynamicSchemePlatform
import cn.super12138.todo.logic.model.PaletteStyle
import cn.super12138.todo.logic.model.SortingMethod
import cn.super12138.todo.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class SettingsViewModel(private val repository: IRepository) : ViewModel() {
    // 把整体Ui状态流拆成3个小流以保证类型安全
    val appearanceUiState: StateFlow<SettingsAppearanceUiState> = combine(
        repository.dynamicColorFlow,
        repository.paletteStyleFlow,
        repository.darkModeFlow,
        repository.pureBlackFlow,
        repository.contrastLevelFlow
    ) { dynamicColor, paletteStyle, darkMode, pureBlackMode, contrastLevel ->
        SettingsAppearanceUiState(
            dynamicColor = dynamicColor,
            paletteStyle = PaletteStyle.fromId(paletteStyle),
            darkMode = DarkMode.fromId(darkMode),
            pureBlackMode = pureBlackMode,
            contrastLevel = ContrastLevel.fromFloat(contrastLevel)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsAppearanceUiState()
    )

    val interfaceUiState: StateFlow<SettingsInterfaceUiState> = combine(
        repository.sortingMethodFlow,
        repository.textFieldAutoFocusFlow,
        repository.secureModeFlow,
        repository.hapticFeedbackFlow
    ) { sortingMethod, textFieldAutoFocus, secureMode, hapticFeedback ->
        SettingsInterfaceUiState(
            sortingMethod = SortingMethod.fromId(sortingMethod),
            textFieldAutoFocus = textFieldAutoFocus,
            secureMode = secureMode,
            hapticFeedback = hapticFeedback
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsInterfaceUiState()
    )

    val dataUiState: StateFlow<SettingsDataUiState> = repository.categoriesFlow.map {
        SettingsDataUiState(categories = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsDataUiState()
    )

    val devUiState: StateFlow<SettingsDevUiState> = combine(
        repository.colorSpecVersionFlow,
        repository.dynamicSchemePlatformFlow
    ) { colorSpecVersion, colorSpecPlatform ->
        SettingsDevUiState(
            colorSpecVersion = ColorSpecVersion.fromId(colorSpecVersion),
            dynamicSchemePlatform = DynamicSchemePlatform.fromId(colorSpecPlatform)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsDevUiState()
    )

    /**
     * 备份应用数据
     *
     * @param uri 备份文件路径的 URI
     * @param context 应用 Context
     * @param onResult 备份完成的回调函数
     */
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

    /**
     * 恢复应用数据
     *
     * @param uri 选择的恢复文件的 URI
     * @param context 应用 Context
     * @param onResult 恢复完成的回调函数
     */
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

    /**
     * 获取要备份文件的文件列表
     * * 数据库文件
     * * 数据库的 wal 文件
     * * 数据库的 shm 文件
     * * DataStore Preferences 文件
     */
    private fun getBackupFiles(context: Context): List<File> {
        val dbPath = context.getDatabasePath(Constants.DB_NAME)
        val prefPath = "${context.filesDir}/datastore"
        return listOf(
            context.getDatabasePath(Constants.DB_NAME), // 数据库
            File("$dbPath-wal"), // 数据库-wal
            File("$dbPath-shm"), // 数据库-shm
            File("$prefPath/${Constants.SP_NAME}.preferences_pb") // DataStore Preferences
        ).filter { it.exists() }
    }

    /**
     * 解压 zip 备份文件
     *
     * @param zipInputStream 备份文件中每个文件的输入流
     * @param context 应用 Context
     */
    private fun extractZipEntries(zipInputStream: ZipInputStream, context: Context) {
        val dbPath = context.getDatabasePath(Constants.DB_NAME).parent
        val prefPath = "${context.filesDir}/datastore/"
        generateSequence { zipInputStream.nextEntry }.forEach { zipEntry ->
            val outputFile = File(
                if (zipEntry.name.endsWith(".preferences_pb")) prefPath else dbPath,
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

    fun setDynamicColor(value: Boolean) {
        viewModelScope.launch {
            repository.setDynamicColor(value)
        }
    }

    fun setPaletteStyle(id: Int) {
        viewModelScope.launch {
            repository.setPaletteStyle(id)
        }
    }

    fun setDarkMode(id: Int) {
        viewModelScope.launch {
            repository.setDarkMode(id)
        }
    }

    fun setPureBlackMode(value: Boolean) {
        viewModelScope.launch {
            repository.setPureBlackMode(value)
        }
    }

    fun setContrastLevel(value: Float) {
        viewModelScope.launch {
            repository.setContrastLevel(value)
        }
    }

    fun setSortingMethod(id: Int) {
        viewModelScope.launch {
            repository.setSortingMethod(id)
        }
    }

    fun setTextFieldAutoFocus(value: Boolean) {
        viewModelScope.launch {
            repository.setTextFieldAutoFocus(value)
        }
    }

    fun setSecureMode(value: Boolean) {
        viewModelScope.launch {
            repository.setSecureMode(value)
        }
    }

    fun setHapticFeedback(value: Boolean) {
        viewModelScope.launch {
            repository.setHapticFeedback(value)
        }
    }

    fun setCategories(categories: List<String>) {
        viewModelScope.launch {
            repository.setCategories(categories)
        }
    }

    fun setColorSpecVersion(id: Int) {
        viewModelScope.launch {
            repository.setColorSpecVersion(id)
        }
    }

    fun setDynamicSchemePlatform(id: Int) {
        viewModelScope.launch {
            repository.setDynamicSchemePlatform(id)
        }
    }
}