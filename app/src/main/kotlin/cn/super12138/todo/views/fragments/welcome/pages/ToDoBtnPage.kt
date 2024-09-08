package cn.super12138.todo.views.fragments.welcome.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentWelcomeTodoBtnBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseFragment

class ToDoBtnPage : BaseFragment<FragmentWelcomeTodoBtnBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentWelcomeTodoBtnBinding {
        return FragmentWelcomeTodoBtnBinding.inflate(inflater, container, attachToRoot)
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