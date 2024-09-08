package cn.super12138.todo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.databinding.FragmentAllTasksBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseFragment
import cn.super12138.todo.views.adapters.AllTasksAdapter
import cn.super12138.todo.views.viewmodels.AllTasksViewModel
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class AllTasksFragment : BaseFragment<FragmentAllTasksBinding>() {
    private val viewModel by viewModels<AllTasksViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoListAll = viewModel.todoListAll
        val layoutManager = LinearLayoutManager(ToDoApp.context)
        binding.allTasksList.layoutManager = layoutManager
        val adapter = AllTasksAdapter(todoListAll, childFragmentManager)
        binding.allTasksList.adapter = adapter

        FastScrollerBuilder(binding.allTasksList).apply {
            useMd2Style()
            build()
        }

        binding.toolBar.setNavigationOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            requireActivity().supportFragmentManager.popBackStack()
        }

        viewModel.refreshData.observe(viewLifecycleOwner, Observer {
            binding.allTasksList.adapter?.notifyItemRangeChanged(0, todoListAll.size + 1)
        })
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentAllTasksBinding {
        return FragmentAllTasksBinding.inflate(inflater, container, attachToRoot)
    }
}