package cn.super12138.todo.views.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.UUID

class ToDoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var ToDoDialogBinding: DialogAddTodoBinding
    private val todoList = ArrayList<ToDo>()

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

        val layoutManager = LinearLayoutManager(ToDoApplication.context)
        binding.todoList.layoutManager = layoutManager
        val adapter = ToDoAdapter(todoList, requireActivity())
        binding.todoList.adapter = adapter

        val progressViewModel =
            ViewModelProvider(requireActivity()).get(ProgressFragmentViewModel::class.java)
        val todoViewModel =
            ViewModelProvider(requireActivity()).get(ToDoFragmentViewModel::class.java)

        lifecycleScope.launch {
            val todos = Repository.getAllUncomplete()
            var count = 0
            for (todo in todos) {
                todoList.add(ToDo(todo.uuid, todo.context, todo.subject))
                count++
            }
            if (count == 0) {
                todoViewModel.emptyTipVis.value = View.VISIBLE
            } else {
                todoViewModel.emptyTipVis.value = View.GONE
            }
        }

        if (todoList.size == 0) {
            todoViewModel.emptyTipVis.value = View.VISIBLE
        }

        binding.addItem.setOnClickListener {
            ToDoDialogBinding = DialogAddTodoBinding.inflate(layoutInflater)

            activity?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle(R.string.add_task)
                    .setView(ToDoDialogBinding.root)
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        val randomUUID = UUID.randomUUID().toString()
                        val todoContext = ToDoDialogBinding.todoContext.editText?.text.toString()
                        val todoSubject = when (ToDoDialogBinding.todoSubject.checkedChipId) {
                            R.id.subject_chinese -> "语文"
                            R.id.subject_math -> "数学"
                            R.id.subject_english -> "英语"
                            R.id.subject_biology -> "生物"
                            R.id.subject_geography -> "地理"
                            R.id.subject_history -> "历史"
                            R.id.subject_physics -> "物理"
                            R.id.subject_law -> "道法"
                            else -> "未知"
                        }

                        // 显示RecyclerView
                        if (todoList.size + 1 > 0) {
                            todoViewModel.emptyTipVis.value = View.GONE
                        }

                        // 添加到RecyclerView
                        todoList.add(
                            ToDo(randomUUID, todoContext, todoSubject)
                        )

                        lifecycleScope.launch {
                            Repository.insert(
                                ToDoRoom(
                                    randomUUID,
                                    0,
                                    todoSubject,
                                    todoContext
                                )
                            )
                            progressViewModel.updateProgress()
                        }

                        binding.todoList.adapter?.notifyItemInserted(todoList.size + 1)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        }

        binding.addItem.setOnLongClickListener {
            activity?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.delete_confirm)
                    .setPositiveButton(R.string.ok) { dialog, which ->
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
                binding.todoList.visibility = View.GONE
                binding.emptyTip.visibility = View.VISIBLE
            } else {
                binding.todoList.visibility = View.VISIBLE
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