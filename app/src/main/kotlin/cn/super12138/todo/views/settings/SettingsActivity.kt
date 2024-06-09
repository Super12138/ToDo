package cn.super12138.todo.views.settings

import android.os.Bundle
import android.view.HapticFeedbackConstants
import cn.super12138.todo.R
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivitySettingsBinding
import cn.super12138.todo.views.BaseActivity

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_frame, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolBar.setNavigationOnClickListener {
            if (GlobalValues.hapticFeedback) {
                it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            }

            finish()
        }
    }

    override fun getViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater)
    }
}