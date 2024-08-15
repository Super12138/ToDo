package cn.super12138.todo.views.all

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivityAllTasksBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseActivity
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class AllTasksActivity : BaseActivity<ActivityAllTasksBinding>() {
    private val viewModel by viewModels<AllTasksViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (GlobalValues.secureMode) {
            true -> window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )

            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        val todoListAll = viewModel.todoListAll
        val layoutManager = LinearLayoutManager(ToDoApp.context)
        binding.allTasksList.layoutManager = layoutManager
        val adapter = AllTasksAdapter(todoListAll, supportFragmentManager)
        binding.allTasksList.adapter = adapter

        FastScrollerBuilder(binding.allTasksList).apply {
            useMd2Style()
            build()
        }

        binding.toolBar.setNavigationOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            finish()
        }

        viewModel.refreshData.observe(this, Observer {
            binding.allTasksList.adapter?.notifyItemRangeChanged(0, todoListAll.size + 1)
        })
    }

    override fun getViewBinding(): ActivityAllTasksBinding {
        return ActivityAllTasksBinding.inflate(layoutInflater)
    }
}