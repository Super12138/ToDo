package cn.super12138.todo.views.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.databinding.DialogAddTodoBinding
import cn.super12138.todo.databinding.FragmentTodoBinding
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.logic.model.ToDo
import cn.super12138.todo.views.progress.ProgressFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import showToast
import java.util.UUID

class ToDoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var ToDoDialogBinding: DialogAddTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*ViewCompat.setOnApplyWindowInsetsListener(binding.addItem) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = if (insets.left == 0) 16 else insets.left
                bottomMargin = if (insets.bottom == 0) 48 else insets.bottom + 32
                rightMargin = if (insets.right == 0) 48 else insets.right + 32
            }
            WindowInsetsCompat.CONSUMED
        }*/

        val progressViewModel =
            ViewModelProvider(requireActivity()).get(ProgressFragmentViewModel::class.java)
        val todoViewModel =
            ViewModelProvider(requireActivity()).get(ToDoFragmentViewModel::class.java)

        val todoList = todoViewModel.todoList

        val layoutManager = LinearLayoutManager(ToDoApplication.context)
        binding.todoList.layoutManager = layoutManager
        val adapter = ToDoAdapter(todoList, requireActivity())
        binding.todoList.adapter = adapter

        FastScrollerBuilder(binding.todoList).apply {
            useMd2Style()
            build()
        }

        if (todoList.isEmpty()) {
            todoViewModel.emptyTipVis.value = View.VISIBLE
        }

        binding.addItem.setOnClickListener {
            ToDoDialogBinding = DialogAddTodoBinding.inflate(layoutInflater)

            activity?.let { it1 ->
                val dialog = MaterialAlertDialogBuilder(it1)
                    .setTitle(R.string.add_task)
                    .setView(ToDoDialogBinding.root)
                    .setPositiveButton(R.string.ok, null)
                    .setNegativeButton(R.string.cancel, null)
                    .show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val todoContent = ToDoDialogBinding.todoContent.editText?.text.toString()
                    if (todoContent.isEmpty()) {
                        ToDoDialogBinding.todoContent.error =
                            getString(R.string.content_cannot_be_empty)
                    } else {
                        if (todoContent == "/DEV MODE") {
                            if (Repository.getPreferenceBoolean(
                                    ToDoApplication.context,
                                    "dev_mode",
                                    true
                                )
                            ) {
                                Repository.setPreference(ToDoApplication.context, "dev_mode", false)
                            } else {
                                Repository.setPreference(ToDoApplication.context, "dev_mode", true)
                                "Dev Mode".showToast()
                            }
                            dialog.dismiss()
                        } else {
                            val randomUUID = UUID.randomUUID().toString()
                            val todoSubject = when (ToDoDialogBinding.todoSubject.checkedChipId) {
                                R.id.subject_chinese -> getString(R.string.subject_chinese)
                                R.id.subject_math -> getString(R.string.subject_math)
                                R.id.subject_english -> getString(R.string.subject_english)
                                R.id.subject_biology -> getString(R.string.subject_biology)
                                R.id.subject_geography -> getString(R.string.subject_geography)
                                R.id.subject_history -> getString(R.string.subject_history)
                                R.id.subject_physics -> getString(R.string.subject_physics)
                                R.id.subject_law -> getString(R.string.subject_law)
                                R.id.subject_other -> getString(R.string.subject_other)
                                else -> getString(R.string.subject_unknown)
                            }

                            // 显示RecyclerView
                            if (todoList.size + 1 > 0) {
                                todoViewModel.emptyTipVis.value = View.GONE
                            }

                            // 添加到RecyclerView
                            todoList.add(
                                ToDo(randomUUID, todoContent, todoSubject)
                            )

                            lifecycleScope.launch {
                                Repository.insert(
                                    ToDoRoom(
                                        randomUUID,
                                        0,
                                        todoSubject,
                                        todoContent
                                    )
                                )
                                progressViewModel.updateProgress()
                                binding.todoList.adapter?.notifyItemInserted(todoList.size + 1)
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }
        }

        binding.addItem.setOnLongClickListener {
            activity?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.delete_confirm)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        todoList.clear()
                        lifecycleScope.launch {
                            Repository.deleteAll()
                            progressViewModel.updateProgress()
                        }
                        binding.todoList.adapter?.notifyItemRangeRemoved(0, todoList.size + 1)

                        todoViewModel.emptyTipVis.value = View.VISIBLE
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            true
        }

        binding.todoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val fab = binding.addItem
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && fab.isShown) {
                    fab.hide()
                }
                if (dy < 0 && !fab.isShown) {
                    fab.show()
                }
            }
        })

        todoViewModel.emptyTipVis.observe(viewLifecycleOwner, Observer { visibility ->
            if (visibility == View.VISIBLE) {
                binding.todoList.alpha = 1f
                binding.todoList.animate().alpha(0f).duration = 200
                binding.todoList.visibility = View.GONE

                binding.emptyTip.alpha = 0f
                binding.emptyTip.visibility = View.VISIBLE
                binding.emptyTip.animate().alpha(1f).duration = 200
            } else {
                binding.todoList.alpha = 0f
                binding.todoList.visibility = View.VISIBLE
                binding.todoList.animate().alpha(1f).duration = 200

                binding.emptyTip.alpha = 1f
                binding.emptyTip.animate().alpha(0f).duration = 200
                binding.emptyTip.visibility = View.GONE
            }
            if (todoList.size == 0 && !binding.addItem.isShown) {
                binding.addItem.show()
            }
        })

        todoViewModel.refreshData.observe(viewLifecycleOwner, Observer {
            binding.todoList.adapter?.notifyItemInserted(todoList.size + 1)
        })
    }
}
