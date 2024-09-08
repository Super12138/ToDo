package cn.super12138.todo.views.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.databinding.FragmentAboutBinding
import cn.super12138.todo.utils.VersionUtils
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.showToast
import cn.super12138.todo.views.BaseFragment

class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    private var clickCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appVersion.text = VersionUtils.getAppVersion(requireActivity())

        binding.toolBar.setNavigationOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.checkUpdate.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constants.UPDATE_URL)
            }
            startActivity(intent)
        }

        binding.openSource.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constants.REPO_GITHUB_URL)
            }
            startActivity(intent)
        }


        binding.developerInfo.setOnClickListener {
            VibrationUtils.performHapticFeedback(it)

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constants.AUTHOR_GITHUB_URL)
            }
            startActivity(intent)
        }

        binding.appVersion.setOnClickListener {
            clickCount++
            when (clickCount) {
                5 -> {
                    clickCount = 0
                    // GlobalValues.springFestivalTheme = !GlobalValues.springFestivalTheme
                    "🍂".showToast()
                    /*when (java.util.Calendar.getInstance(Locale.getDefault())
                        .get(java.util.Calendar.MONTH) + 1) {
                        3, 4, 5 ->
                        6, 7, 8 -> SUMMER
                        9, 10, 11 -> AUTUMN
                        12, 1, 2 -> WINTER
                        else -> -12
                    }*/
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, attachToRoot)
    }
}