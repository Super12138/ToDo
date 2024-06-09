package cn.super12138.todo.views.bottomsheet

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import cn.super12138.todo.R
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.BottomSheetTodoBinding
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.utils.TextUtils
import cn.super12138.todo.utils.showToast
import cn.super12138.todo.utils.toEditable
import cn.super12138.todo.views.progress.ProgressFragmentViewModel
import cn.super12138.todo.views.todo.ToDoFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.UUID

class ToDoBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetTodoBinding

    private val progressViewModel: ProgressFragmentViewModel by viewModels({ requireActivity() })
    private val todoViewModel: ToDoFragmentViewModel by viewModels({ requireActivity() })

    private var editMode: Boolean = false
    private var todoState: Int = 0
    private var todoPosition: Int = 0
    private lateinit var todoUUID: String
    private lateinit var todoOrigSubject: String
    private lateinit var todoOrigContent: String

    companion object {
        const val TAG = Constants.TAG_TODO_BOTTOM_SHEET

        fun newInstance(
            editMode: Boolean,
            todoPosition: Int,
            todoUUID: String,
            todoState: Int,
            todoSubject: String,
            todoContent: String,
        ) = ToDoBottomSheet().apply {
            arguments = Bundle().apply {
                putBoolean(Constants.BUNDLE_EDIT_MODE, editMode)
                putInt(Constants.BUNDLE_POSITION, todoPosition)
                putString(Constants.BUNDLE_TODO_UUID, todoUUID)
                putInt(Constants.BUNDLE_TODO_STATE, todoState)
                putString(Constants.BUNDLE_TODO_SUBJECT, todoSubject)
                putString(Constants.BUNDLE_TODO_CONTENT, todoContent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editMode = it.getBoolean(Constants.BUNDLE_EDIT_MODE, false)
            todoPosition = it.getInt(Constants.BUNDLE_POSITION, 0)
            todoOrigContent = it.getString(Constants.BUNDLE_TODO_CONTENT, "")
            todoOrigSubject = it.getString(Constants.BUNDLE_TODO_SUBJECT, "")
            todoState = it.getInt(Constants.BUNDLE_TODO_STATE, 0)
            todoUUID = it.getString(Constants.BUNDLE_TODO_UUID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetTodoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BottomSheet 基本参数设置
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            saveFlags = BottomSheetBehavior.SAVE_ALL
        }
        bottomSheetDialog.dismissWithAnimation = true

        val todoList = todoViewModel.todoList

        // 编辑模式
        if (editMode) {
            binding.btnCancel.visibility = View.GONE
            binding.btnDelete.visibility = View.VISIBLE
            binding.todoSheetTitle.text = getString(R.string.update_task)
            binding.todoContent.editText?.text = todoOrigContent.toEditable()
            binding.btnSave.text = getString(R.string.update)

            TextUtils.getSubjectID(todoOrigSubject)?.let { binding.todoSubject.check(it) }
        }

        binding.btnSave.setOnClickListener {
            if (GlobalValues.hapticFeedback) {
                it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            }

            val todoContent = binding.todoContent.editText?.text.toString()
            // 内容判空
            if (todoContent.isEmpty()) {
                binding.todoContent.error =
                    getString(R.string.content_cannot_be_empty)
                return@setOnClickListener
            } else {
                // 开发者模式
                if (todoContent == Constants.STRING_DEV_MODE) {
                    if (GlobalValues.devMode) {
                        GlobalValues.devMode = false
                    } else {
                        GlobalValues.devMode = true
                        "Dev Mode".showToast()
                    }
                } else {
                    // 随机 UUID
                    val randomUUID = UUID.randomUUID().toString()
                    // 待办学科
                    val todoSubject = TextUtils.getSubjectName(binding.todoSubject.checkedChipId)

                    // 更新待办
                    if (editMode) {
                        todoViewModel.updateTask(
                            todoPosition,
                            ToDoRoom(
                                todoUUID,
                                todoState,
                                todoSubject,
                                todoContent
                            )
                        )
                        todoViewModel.refreshData.value = 1
                    } else {
                        // 添加到 RecyclerView
                        if (todoList.size + 1 > 0) {
                            todoViewModel.emptyTipVis.value = View.GONE
                        }
                        todoList.add(
                            ToDo(randomUUID, 0, todoContent, todoSubject)
                        )

                        // 插入数据库
                        todoViewModel.insertTask(
                            ToDoRoom(
                                randomUUID,
                                0,
                                todoSubject,
                                todoContent
                            )
                        )
                        progressViewModel.updateProgress()
                        todoViewModel.addData.value = 1
                    }
                }
            }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            if (GlobalValues.hapticFeedback) {
                it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            }

            dismiss()
        }

        binding.btnDelete.setOnClickListener {
            if (GlobalValues.hapticFeedback) {
                it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            }

            todoViewModel.deleteTask(todoPosition, todoUUID)
            progressViewModel.updateProgress()
            todoViewModel.removeData.value = 1

            // 空项目提示显示判断
            if (todoList.isEmpty()) {
                todoViewModel.emptyTipVis.value = View.VISIBLE
            } else {
                todoViewModel.emptyTipVis.value = View.GONE
            }

            dismiss()
        }
    }
}