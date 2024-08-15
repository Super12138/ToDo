package cn.super12138.todo.views.welcome.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeTodoBtnBinding
import cn.super12138.todo.utils.VibrationUtils

class ToDoBtnPage : BasePage() {
    private lateinit var binding: FragmentWelcomeTodoBtnBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeTodoBtnBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addItem.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)
        }

        binding.addItem.setOnLongClickListener {
            true
        }
    }
}