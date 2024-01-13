package cn.super12138.todo.views.settings

import android.os.Bundle
import android.os.Process
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cn.super12138.todo.R
import cn.super12138.todo.databinding.ActivitySettingsBinding
import cn.super12138.todo.views.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_frame, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            findPreference<ListPreference>("dark_mode")?.apply {
                setOnPreferenceChangeListener { preference, newValue ->
                    when (newValue) {
                        "0" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                        "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    activity?.recreate()
                    true
                }
            }

            findPreference<SwitchPreferenceCompat>("secure_mode")?.apply {
                setOnPreferenceChangeListener { preference, newValue ->
                    when (newValue) {
                        true -> activity?.window?.setFlags(
                            WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE
                        )

                        false -> activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                    }
                    view?.let {
                        Snackbar.make(it, R.string.need_restart_app, Snackbar.LENGTH_LONG)
                            .setAction(R.string.restart_app_now) {
                                Process.killProcess(Process.myPid())
                                exitProcess(10)
                            }
                            .show()
                    }

                    true
                }
            }
        }
    }
}