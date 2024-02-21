package cn.super12138.todo.views.settings

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cn.super12138.todo.R
import cn.super12138.todo.ToDoApplication
import cn.super12138.todo.databinding.ActivitySettingsBinding
import cn.super12138.todo.databinding.DialogBackupBinding
import cn.super12138.todo.databinding.DialogRestoreBinding
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.views.BaseActivity
import cn.super12138.todo.views.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
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
        private lateinit var backupBinding: DialogBackupBinding
        private lateinit var restoreBinding: DialogRestoreBinding
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            val gson = Gson()
            val isDevMode =
                Repository.getPreferenceBoolean(ToDoApplication.context, "dev_mode", false)

            findPreference<ListPreference>("dark_mode")?.apply {
                setOnPreferenceChangeListener { _, newValue ->
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
                setOnPreferenceChangeListener { _, _ ->
                    view?.let {
                        Snackbar.make(it, R.string.need_restart_app, Snackbar.LENGTH_LONG)
                            .setAction(R.string.restart_app_now) {
                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                context.startActivity(intent)
                                exitProcess(0)
                            }
                            .show()
                    }

                    true
                }
            }

            findPreference<Preference>("backupDB")?.apply {
                setOnPreferenceClickListener {
                    lifecycleScope.launch {
                        val data = Repository.getAll()
                        val jsonData = gson.toJson(data)
                        backupBinding = DialogBackupBinding.inflate(layoutInflater)
                        backupBinding.jsonOutput.text = jsonData

                        activity?.let {
                            MaterialAlertDialogBuilder(it)
                                .setTitle(R.string.export_data)
                                .setView(backupBinding.root)
                                .setPositiveButton(R.string.ok, null)
                                .show()
                        }
                    }
                    true
                }
            }

            findPreference<Preference>("restoreDB")?.apply {
                setOnPreferenceClickListener {
                    restoreBinding = DialogRestoreBinding.inflate(layoutInflater)
                    activity?.let { it1 ->
                        val dialog = MaterialAlertDialogBuilder(it1)
                            .setTitle(R.string.restore_data)
                            .setView(restoreBinding.root)
                            .setPositiveButton(R.string.ok, null)
                            .setNegativeButton(R.string.cancel, null)
                            .show()

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            val jsonInput = restoreBinding.jsonInput.editText?.text.toString()
                            if (jsonInput.isEmpty()) {
                                restoreBinding.jsonInput.error =
                                    getString(R.string.please_paste_data)
                                return@setOnClickListener
                            }

                            lifecycleScope.launch {
                                if (restoreBinding.overwriteData.isChecked) {
                                    Repository.deleteAll()
                                }
                                try {
                                    val listType = object : TypeToken<List<ToDoRoom>>() {}.type
                                    val taskList: List<ToDoRoom> =
                                        gson.fromJson(jsonInput, listType)
                                    taskList.forEach { Repository.insert(it) }
                                    dialog.dismiss()
                                    MaterialAlertDialogBuilder(it1)
                                        .setTitle(R.string.restore_successful)
                                        .setMessage(R.string.restore_need_restart_app)
                                        .setPositiveButton(R.string.ok) { _, _ ->
                                            Process.killProcess(Process.myPid())
                                            exitProcess(10)
                                        }
                                        .setNegativeButton(R.string.cancel, null)
                                        .setCancelable(false)
                                        .show()
                                } catch (e: JsonSyntaxException) {
                                    restoreBinding.jsonInput.error =
                                        getString(R.string.json_data_incorrect)
                                } catch (e: Exception) {
                                    if (isDevMode) {
                                        restoreBinding.jsonInput.error = e.toString()
                                    } else {
                                        restoreBinding.jsonInput.error =
                                            getString(R.string.restore_failed)
                                    }
                                }
                            }
                        }
                    }
                    true
                }
            }
        }
    }
}