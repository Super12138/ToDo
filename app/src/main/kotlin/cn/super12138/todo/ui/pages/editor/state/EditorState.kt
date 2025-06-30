package cn.super12138.todo.ui.pages.editor.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cn.super12138.todo.logic.database.TodoEntity
import cn.super12138.todo.logic.model.Subjects

class EditorState(
    val initialTodo: TodoEntity? = null,
) {
    var toDoContent by mutableStateOf(initialTodo?.content ?: "")
    var isErrorContent by mutableStateOf(false)
    var selectedSubjectId by mutableIntStateOf(initialTodo?.subject ?: 0)
    var subjectContent by mutableStateOf(initialTodo?.customSubject ?: "")
    var isErrorSubject by mutableStateOf(false)
    var priorityState by mutableFloatStateOf(initialTodo?.priority ?: 0f)
    var isCompleted by mutableStateOf(initialTodo?.isCompleted == true)
    var showExitConfirmDialog by mutableStateOf(false)
    var showDeleteConfirmDialog by mutableStateOf(false)

    /**
     * 检查待办内容和学科的一者是否无有效，如果无效则为文本框设置错误状态
     *
     * @return 它们二者是否都有效。有一者无效就返回 true
     */
    fun setErrorIfNotValid(): Boolean {
        isErrorContent = toDoContent.trim().isEmpty()
        isErrorSubject = subjectContent.trim().isEmpty() &&
                selectedSubjectId == Subjects.Custom.id
        return isErrorContent || isErrorSubject
    }

    /**
     * 清除文本框全部错误
     */
    fun clearError() {
        isErrorContent = false
        isErrorSubject = false
    }

    /**
     * 获取编辑后的待办实体
     *
     * @return TodoEntity 待办实体
     */
    fun getEntity(): TodoEntity = TodoEntity(
        id = initialTodo?.id ?: 0,
        content = toDoContent,
        subject = selectedSubjectId,
        customSubject = subjectContent,
        priority = priorityState,
        isCompleted = isCompleted
    )

    /**
     * 检查待办是否被编辑修改
     */
    fun isModified(): Boolean {
        var isModified = false
        if ((initialTodo?.content ?: "") != toDoContent) isModified = true
        if ((initialTodo?.subject ?: 0) != selectedSubjectId) isModified = true
        if ((initialTodo?.customSubject ?: "") != subjectContent) isModified = true
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
                value.selectedSubjectId,
                value.subjectContent,
                value.isErrorSubject,
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
                selectedSubjectId = list[3] as Int
                subjectContent = list[4] as String
                isErrorSubject = list[5] as Boolean
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