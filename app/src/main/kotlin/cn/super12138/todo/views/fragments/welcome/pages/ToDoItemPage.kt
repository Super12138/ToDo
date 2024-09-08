package cn.super12138.todo.views.fragments.welcome.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeTodoItemBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseFragment

class ToDoItemPage : BaseFragment<FragmentWelcomeTodoItemBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentWelcomeTodoItemBinding {
        return FragmentWelcomeTodoItemBinding.inflate(inflater, container, attachToRoot)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkItemBtn.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)
        }
        binding.todoItem.setOnLongClickListener {
            true
        }
    }
}