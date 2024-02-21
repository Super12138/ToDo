package cn.super12138.todo.views.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.databinding.BottomSheetInfoBinding
import cn.super12138.todo.logic.Repository
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
        const val TAG = "InfoBtmSheet"
        fun newInstance(
            todoContent: String,
            todoSubject: String,
            todoState: Int,
            todoUUID: String
        ) = InfoBottomSheet().apply {
            arguments = Bundle().apply {
                putString("todoContent", todoContent)
                putString("todoSubject", todoSubject)
                putInt("todoState", todoState)
                putString("todoUUID", todoUUID)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoContent = it.getString("todoContent", "")
            todoSubject = it.getString("todoSubject", "")
            todoState = it.getInt("todoState", 0)
            todoUUID = it.getString("todoUUID", "")
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

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.dismissWithAnimation = true

        binding.todoContentInfo.text = todoContent
        binding.todoSubjectInfo.text = String.format(getString(R.string.info_subject), todoSubject)
        if (todoState == 0) {
            binding.todoState.text = getString(R.string.info_state_complete)
        } else {
            binding.todoState.text = getString(R.string.info_state_incomplete)
        }
        if (Repository.getPreferenceBoolean(ToDoApplication.context, "dev_mode", false)) {
            binding.todoUuid.apply {
                visibility = View.VISIBLE
                text = String.format(getString(R.string.info_uuid), todoUUID)
            }
        }
    }
}