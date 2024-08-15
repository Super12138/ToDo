package cn.super12138.todo.views.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import cn.super12138.todo.R
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivityWelcomeBinding
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.utils.setOnIntervalClickListener
import cn.super12138.todo.views.BaseActivity
import cn.super12138.todo.views.main.MainActivity
import cn.super12138.todo.views.welcome.pages.IntroPage
import cn.super12138.todo.views.welcome.pages.ProgressPage
import cn.super12138.todo.views.welcome.pages.ToDoBtnPage
import cn.super12138.todo.views.welcome.pages.ToDoItemPage

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    private val viewModel by viewModels<WelcomeViewModel>()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val previousBtn = binding.previousBtn
        val nextBtn = binding.nextBtn
        val centerBtn = binding.centerBtn

        val currentPage = viewModel.currentPage // 0: Intro 1: Progress 2: ToDo Btn 3: ToDo Item

        centerBtn.setOnIntervalClickListener {
            VibrationUtils.performHapticFeedback(it)

            centerBtn.hide()
            nextBtn.show()
            previousBtn.show()


            if (currentPage.value == 3) {
                GlobalValues.welcomePage = true
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                currentPage.value = 1
            }
        }

        previousBtn.setOnIntervalClickListener {
            VibrationUtils.performHapticFeedback(it)

            currentPage.value = currentPage.value?.minus(1)
        }

        nextBtn.setOnIntervalClickListener {
            VibrationUtils.performHapticFeedback(it)

            currentPage.value = currentPage.value?.plus(1)
        }

        currentPage.observe(this, Observer { page ->
            supportFragmentManager.commit {
                addToBackStack(System.currentTimeMillis().toString())
                hide(supportFragmentManager.fragments.last())
                add(R.id.welcome_page_container, getCurrentPage(page))
            }

            when (page) {
                0 -> {
                    centerBtn.apply {
                        text = getString(R.string.start)
                        icon = AppCompatResources.getDrawable(
                            this@WelcomeActivity,
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
                            this@WelcomeActivity,
                            R.drawable.ic_focus
                        )
                        show()
                    }
                    previousBtn.show()
                    nextBtn.hide()
                }

                else -> {
                    if (page > 3) currentPage.value = 3

                    if (page < 0) currentPage.value = 0
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentPage.value == 0) {
                    finish()
                } else {
                    currentPage.value = currentPage.value?.minus(1)
                }
            }
        })
    }

    override fun getViewBinding(): ActivityWelcomeBinding {
        return ActivityWelcomeBinding.inflate(layoutInflater)
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
}