package cn.super12138.todo.views.welcome.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeIntroBinding

class IntroPage : BasePage() {
    private lateinit var binding: FragmentWelcomeIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeIntroBinding.inflate(inflater, container, false)

        return binding.root
    }
}