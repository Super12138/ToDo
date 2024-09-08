package cn.super12138.todo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.R
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.BottomSheetInfoBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetInfoBinding
    private lateinit var todoContent: String
    private lateinit var todoSubject: String
    private var todoState: Int = 0
    private lateinit var todoUUID: String

    companion object {
        const val TAG = Constants.TAG_INFO_BOTTOM_SHEET
        fun newInstance(
            todoContent: String,
            todoSubject: String,
            todoState: Int,
            todoUUID: String
        ) = InfoBottomSheet().apply {
            arguments = Bundle().apply {
                putString(Constants.BUNDLE_TODO_CONTENT, todoContent)
                putString(Constants.BUNDLE_TODO_SUBJECT, todoSubject)
                putInt(Constants.BUNDLE_TODO_STATE, todoState)
                putString(Constants.BUNDLE_TODO_UUID, todoUUID)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoContent = it.getString(Constants.BUNDLE_TODO_CONTENT, "")
            todoSubject = it.getString(Constants.BUNDLE_TODO_SUBJECT, "")
            todoState = it.getInt(Constants.BUNDLE_TODO_STATE, 0)
            todoUUID = it.getString(Constants.BUNDLE_TODO_UUID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.dismissWithAnimation = true

        binding.todoContentInfo.text = todoContent
        binding.todoSubjectInfo.text = String.format(getString(R.string.info_subject), todoSubject)
        if (todoState == 0) {
            binding.todoState.text = getString(R.string.info_state_incomplete)
        } else {
            binding.todoState.text = getString(R.string.info_state_complete)
        }
        if (GlobalValues.devMode) {
            binding.todoUuid.apply {
                visibility = View.VISIBLE
                text = String.format(getString(R.string.info_uuid), todoUUID)
            }
        }
    }
}