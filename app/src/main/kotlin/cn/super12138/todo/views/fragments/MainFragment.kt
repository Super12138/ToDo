package cn.super12138.todo.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.R
import cn.super12138.todo.databinding.FragmentMainBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.BaseFragment
import cn.super12138.todo.views.activities.MainActivity

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_settings -> {
                    (requireActivity() as MainActivity).startFragment(SettingsParentFragment())

                    VibrationUtils.performHapticFeedback(binding.toolBar)

                    true
                }

                else -> false
            }
        }

        // setSupportActionBar(binding.toolbar)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, attachToRoot)
    }
}