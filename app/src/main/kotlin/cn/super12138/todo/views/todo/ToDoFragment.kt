package cn.super12138.todo.views.todo

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
import cn.super12138.todo.views.bottomsheet.ToDoBottomSheet
import cn.super12138.todo.views.progress.ProgressFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class ToDoFragment : Fragment() {
    private val progressViewModel: ProgressFragmentViewModel by viewModels({ requireActivity() })
    private val todoViewModel: ToDoFragmentViewModel by viewModels({ requireActivity() })
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
        val todoList = todoViewModel.todoList

        val layoutManager = LinearLayoutManager(ToDoApp.context)
        binding.todoList.layoutManager = layoutManager
        val adapter = ToDoAdapter(todoList, requireActivity(), parentFragmentManager)
        binding.todoList.adapter = adapter

        FastScrollerBuilder(binding.todoList).apply {
            useMd2Style()
            build()
        }

        if (todoList.isEmpty()) {
            todoViewModel.emptyTipVis.value = View.VISIBLE
        }

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
                        binding.todoList.adapter?.notifyItemRangeRemoved(0, todoList.size + 1)

                        // 显示空项目提示
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

        todoViewModel.addData.observe(viewLifecycleOwner, Observer {
            binding.todoList.adapter?.notifyItemInserted(todoList.size + 1)
        })

        todoViewModel.removeData.observe(viewLifecycleOwner, Observer {
            binding.todoList.adapter?.notifyItemRemoved(todoList.size + 1)
        })

        todoViewModel.refreshData.observe(viewLifecycleOwner, Observer {
            binding.todoList.adapter?.notifyItemRangeChanged(0, todoList.size + 1)
        })
    }
}
