package cn.super12138.todo.ui.pages.editor.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cn.super12138.todo.R
import cn.super12138.todo.logic.database.TodoEntity

class EditorState(val initialTodo: TodoEntity? = null) {
    var toDoContent by mutableStateOf(initialTodo?.content ?: "")
    var isErrorContent by mutableStateOf(false)
    var selectedCategoryIndex by mutableIntStateOf(-1)
    var categoryContent by mutableStateOf(initialTodo?.category ?: "")
    var isErrorCategory by mutableStateOf(false)
    var priorityState by mutableFloatStateOf(initialTodo?.priority ?: 0f)
    var isCompleted by mutableStateOf(initialTodo?.isCompleted == true)

    var categorySupportingText by mutableIntStateOf(R.string.tip_max_length_5)
        private set

    var showExitConfirmDialog by mutableStateOf(false)
    var showDeleteConfirmDialog by mutableStateOf(false)

    /**
     * 检查待办内容和学科的一者是否无有效，如果无效则为文本框设置错误状态
     *
     * @return 它们二者是否都有效。有一者无效就返回 true
     */
    fun setErrorIfNotValid(): Boolean {
        isErrorContent = toDoContent.trim().isEmpty()
        if (selectedCategoryIndex == -1) {
            if (categoryContent.trim().isEmpty()) {
                isErrorCategory = true
                categorySupportingText = R.string.error_no_content_entered
            } else if (categoryContent.length > 5) {
                isErrorCategory = true
                categorySupportingText = R.string.error_exceeds_5_chars
            } else {
                isErrorCategory = false
                categorySupportingText = R.string.tip_max_length_5
            }
        } else {
            isErrorCategory = false
        }
        return isErrorContent || isErrorCategory
    }

    /**
     * 清除文本框全部错误
     */
    fun clearError() {
        isErrorContent = false
        isErrorCategory = false
    }

    /**
     * 检查待办是否被编辑修改
     */
    fun isModified(): Boolean {
        var isModified = false
        if ((initialTodo?.content ?: "") != toDoContent) isModified = true
        if ((initialTodo?.category ?: "") != categoryContent) isModified = true
        if ((initialTodo?.priority ?: 0f) != priorityState) isModified = true
        if ((initialTodo?.isCompleted == true) != isCompleted) isModified = true
        return isModified
    }

    /**
     * 保存状态的 Saver 对象，用于适配 rememberSaveable
     */
    object Saver : androidx.compose.runtime.saveable.Saver<EditorState, Any> {
        override fun SaverScope.save(value: EditorState): Any? {
            return listOf(
                value.initialTodo?.id ?: 0,
                value.toDoContent,
                value.isErrorContent,
                value.selectedCategoryIndex,
                value.categoryContent,
                value.isErrorCategory,
                value.priorityState,
                value.isCompleted,
                value.showExitConfirmDialog,
                value.showDeleteConfirmDialog
            )
        }

        override fun restore(value: Any): EditorState? {
            val list = value as List<*>
            val initialTodo = list[0] as? TodoEntity?
            return EditorState(initialTodo).apply {
                toDoContent = list[1] as String
                isErrorContent = list[2] as Boolean
                selectedCategoryIndex = list[3] as Int
                categoryContent = list[4] as String
                isErrorCategory = list[5] as Boolean
                priorityState = list[6] as Float
                isCompleted = list[7] as Boolean
                showExitConfirmDialog = list[8] as Boolean
                showDeleteConfirmDialog = list[9] as Boolean
            }
        }
    }
}

@Composable
fun rememberEditorState(initialTodo: TodoEntity? = null): EditorState =
    rememberSaveable(saver = EditorState.Saver) { EditorState(initialTodo) }