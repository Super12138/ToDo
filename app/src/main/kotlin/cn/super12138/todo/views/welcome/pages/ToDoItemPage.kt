package cn.super12138.todo.views.welcome.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeTodoItemBinding
import cn.super12138.todo.utils.VibrationUtils

class ToDoItemPage : BasePage() {
    private lateinit var binding: FragmentWelcomeTodoItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeTodoItemBinding.inflate(inflater, container, false)

        return binding.root
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