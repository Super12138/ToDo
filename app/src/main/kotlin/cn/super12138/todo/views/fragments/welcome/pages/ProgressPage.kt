package cn.super12138.todo.views.fragments.welcome.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeProgressBinding
import cn.super12138.todo.views.BaseFragment

class ProgressPage : BaseFragment<FragmentWelcomeProgressBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentWelcomeProgressBinding {
        return FragmentWelcomeProgressBinding.inflate(inflater, container, attachToRoot)
    }
}