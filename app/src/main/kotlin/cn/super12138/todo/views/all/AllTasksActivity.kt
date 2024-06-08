package cn.super12138.todo.views.all

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.super12138.todo.ToDoApp
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivityAllTasksBinding
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

        val layoutManager = LinearLayoutManager(ToDoApp.context)
        binding.allTasksList.layoutManager = layoutManager
        val adapter = AllTasksAdapter(viewModel.todoListAll, supportFragmentManager)
        binding.allTasksList.adapter = adapter

        FastScrollerBuilder(binding.allTasksList).apply {
            useMd2Style()
            build()
        }

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        viewModel.emptyTipVis.observe(this, Observer { visibility ->
            if (visibility == View.VISIBLE) {
                binding.allTasksList.alpha = 1f
                binding.allTasksList.animate().alpha(0f).duration = 200
                binding.allTasksList.visibility = View.GONE

                binding.emptyTip.alpha = 0f
                binding.emptyTip.visibility = View.VISIBLE
                binding.emptyTip.animate().alpha(1f).duration = 200
            } else {
                binding.allTasksList.alpha = 0f
                binding.allTasksList.visibility = View.VISIBLE
                binding.allTasksList.animate().alpha(1f).duration = 200

                binding.emptyTip.alpha = 1f
                binding.emptyTip.animate().alpha(0f).duration = 200
                binding.emptyTip.visibility = View.GONE
            }
        })
    }

    override fun getViewBinding(): ActivityAllTasksBinding {
        return ActivityAllTasksBinding.inflate(layoutInflater)
    }
}