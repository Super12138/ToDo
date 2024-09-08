package cn.super12138.todo.views.activities
// 2023.11.18立项
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import cn.super12138.todo.R
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.ActivityMainBinding
import cn.super12138.todo.views.BaseActivity
import cn.super12138.todo.views.fragments.SettingsParentFragment
import cn.super12138.todo.views.fragments.welcome.WelcomeFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (!GlobalValues.welcomePage) {
            startFragment(WelcomeFragment())
        }

        when (GlobalValues.secureMode) {
            true -> window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )

            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        handleIntent(intent)
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    fun startFragment(fragment: Fragment, args: (Bundle.() -> Unit)? = null) {
        supportFragmentManager.commit {
            addToBackStack(System.currentTimeMillis().toString())
            hide(supportFragmentManager.fragments.last())
            add(
                R.id.app_container,
                fragment.apply { args?.let { arguments = Bundle().apply(it) } }
            )
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_APPLICATION_PREFERENCES -> startFragment(SettingsParentFragment())
        }
    }
}