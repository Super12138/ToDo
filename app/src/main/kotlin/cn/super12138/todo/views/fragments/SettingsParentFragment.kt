package cn.super12138.todo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.databinding.FragmentSettingsBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseFragment

class SettingsParentFragment : BaseFragment<FragmentSettingsBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setNavigationOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, attachToRoot)
    }
}