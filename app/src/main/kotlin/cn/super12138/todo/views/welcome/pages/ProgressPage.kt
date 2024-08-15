package cn.super12138.todo.views.welcome.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeProgressBinding

class ProgressPage : BasePage() {
    private lateinit var binding: FragmentWelcomeProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeProgressBinding.inflate(inflater, container, false)

        return binding.root
    }
}