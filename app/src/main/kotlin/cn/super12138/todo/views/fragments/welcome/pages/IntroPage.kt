package cn.super12138.todo.views.fragments.welcome.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeIntroBinding
import cn.super12138.todo.views.BaseFragment

class IntroPage : BaseFragment<FragmentWelcomeIntroBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentWelcomeIntroBinding {
        return FragmentWelcomeIntroBinding.inflate(inflater, container, attachToRoot)
    }
}