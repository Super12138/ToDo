package cn.super12138.todo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.databinding.FragmentTodoBinding
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.adapters.ToDoAdapter
import cn.super12138.todo.views.viewmodels.ProgressViewModel
import cn.super12138.todo.views.viewmodels.ToDoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class ToDoFragment : Fragment() {
    private val progressViewModel: ProgressViewModel by viewModels({ requireActivity() })
    private val todoViewModel: ToDoViewModel by viewModels({ requireActivity() })
    private lateinit var binding: FragmentTodoBinding

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
        ViewCompat.setOnApplyWindowInsetsListener(binding.addItem) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = if (insets.left == 0) 16 else insets.left
                bottomMargin = if (insets.bottom == 0) 48 else insets.bottom + 32
                rightMargin = if (insets.right == 0) 48 else insets.right + 32
            }
            WindowInsetsCompat.CONSUMED
        }

        FastScrollerBuilder(binding.todoList).apply {
            useMd2Style()
            build()
        }

        val layoutManager = LinearLayoutManager(ToDoApp.context)
        binding.todoList.layoutManager = layoutManager
        val todoList = todoViewModel.todoList
        val adapter = ToDoAdapter(todoList, requireActivity(), parentFragmentManager)
        binding.todoList.adapter = adapter

        binding.addItem.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            val toDoBottomSheet = ToDoBottomSheet()
            toDoBottomSheet.show(parentFragmentManager, ToDoBottomSheet.TAG)
        }

        binding.addItem.setOnLongClickListener {
            activity?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.delete_confirm)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        VibrationUtils.performHapticFeedback(it)

                        // 清除 Recycler View
                        todoList.clear()
                        // 清除数据库
                        lifecycleScope.launch {
                            Repository.deleteAll()
                            progressViewModel.updateProgress()
                        }
                        // 通知数据更改
                        binding.todoList.adapter?.notifyDataSetChanged()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            true
        }

        binding.todoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val fab = binding.addItem
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 列表下滑，隐藏FAB
                if (dy > 0 && fab.isShown) {
                    fab.hide()
                }
                // 列表上滑，显示FAB
                if (dy < 0 && !fab.isShown) {
                    fab.show()
                }
            }
        })

        todoViewModel.addData.observe(viewLifecycleOwner) {
            binding.todoList.adapter?.notifyItemInserted(todoList.size + 1)
        }

        todoViewModel.removeData.observe(viewLifecycleOwner) {
            binding.todoList.adapter?.notifyItemRemoved(todoList.size + 1)
        }

        todoViewModel.refreshData.observe(viewLifecycleOwner) {
            binding.todoList.adapter?.notifyItemRangeChanged(0, todoList.size + 1)
        }
    }
}
