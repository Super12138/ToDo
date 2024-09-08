package cn.super12138.todo.views.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.super12138.todo.R
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.FragmentWelcomeBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.setOnIntervalClickListener
import cn.super12138.todo.views.BaseFragment
import cn.super12138.todo.views.activities.MainActivity
import cn.super12138.todo.views.fragments.MainFragment
import cn.super12138.todo.views.fragments.welcome.pages.IntroPage
import cn.super12138.todo.views.fragments.welcome.pages.ProgressPage
import cn.super12138.todo.views.fragments.welcome.pages.ToDoBtnPage
import cn.super12138.todo.views.fragments.welcome.pages.ToDoItemPage
import cn.super12138.todo.views.viewmodels.WelcomeViewModel
import kotlinx.coroutines.launch

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    private val viewModel by viewModels<WelcomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previousBtn = binding.previousBtn
        val nextBtn = binding.nextBtn
        val centerBtn = binding.centerBtn

        val currentPage = viewModel.currentPage // 0: Intro 1: Progress 2: ToDo Btn 3: ToDo Item

        // 返回回调
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPage.value == 0) {
                    requireActivity().finishAffinity()
                } else {
                    childFragmentManager.popBackStack()
                    viewModel.decreasePage()
                }
            }
        }


        centerBtn.setOnIntervalClickListener {
            VibrationUtils.performHapticFeedback(it)

            centerBtn.hide()
            nextBtn.show()
            previousBtn.show()

            if (currentPage.value == 3) {
                GlobalValues.welcomePage = true
                (requireActivity() as MainActivity).startFragment(MainFragment())
            } else {
                viewModel.setCurrentPage(1)
                nextPage(1)
            }
        }

        previousBtn.setOnIntervalClickListener {
            VibrationUtils.performHapticFeedback(it)

            childFragmentManager.popBackStack()

            viewModel.decreasePage()
        }

        nextBtn.setOnIntervalClickListener {
            VibrationUtils.performHapticFeedback(it)

            nextPage(currentPage.value + 1)

            callback.isEnabled = false
            viewModel.increasePage()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentPage.collect { page ->
                    when (page) {
                        0 -> {
                            centerBtn.apply {
                                text = getString(R.string.start)
                                icon = AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_arrow_forward
                                )
                                show()
                            }
                            nextBtn.hide()
                            previousBtn.hide()
                        }

                        in 1..2 -> {
                            centerBtn.hide()
                            previousBtn.show()
                            nextBtn.show()
                        }

                        3 -> {
                            centerBtn.apply {
                                text = getString(R.string.enter_app)
                                icon = AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_focus
                                )
                                show()
                            }
                            previousBtn.show()
                            nextBtn.hide()
                        }

                        else -> {
                            if (page > 3) viewModel.setCurrentPage(3)

                            if (page < 0) viewModel.setCurrentPage(0)
                        }
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun getCurrentPage(pageIndex: Int): Fragment {
        return when (pageIndex) {
            0 -> IntroPage()
            1 -> ProgressPage()
            2 -> ToDoBtnPage()
            3 -> ToDoItemPage()
            else -> IntroPage()
        }
    }

    private fun nextPage(page: Int) {
        childFragmentManager.commit {
            addToBackStack(System.currentTimeMillis().toString())
            hide(childFragmentManager.fragments.last())
            add(R.id.welcome_page_container, getCurrentPage(page))
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater, container, attachToRoot)
    }
}