package cn.super12138.todo.views.settings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.HapticFeedbackConstants
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cn.super12138.todo.R
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.databinding.DialogBackupBinding
import cn.super12138.todo.databinding.DialogRestoreBinding
import cn.super12138.todo.logic.Repository
import cn.super12138.todo.logic.dao.ToDoRoom
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.about.AboutActivity
import cn.super12138.todo.views.all.AllTasksActivity
import cn.super12138.todo.views.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var backupBinding: DialogBackupBinding
    private lateinit var restoreBinding: DialogRestoreBinding

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val gson = Gson()

        findPreference<ListPreference>(Constants.PREF_DARK_MODE)?.apply {
            setOnPreferenceClickListener {
                view?.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                true
            }
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

        findPreference<SwitchPreferenceCompat>(Constants.PREF_SECURE_MODE)?.apply {
            setOnPreferenceChangeListener { _, _ ->
                view?.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)


                view?.let {
                    Snackbar.make(it, R.string.need_restart_app, Snackbar.LENGTH_LONG)
                        .setAction(R.string.restart_app_now) {
                            VibrationUtils.performHapticFeedback(view)
                            restartApp(context)
                        }
                        .show()
                }

                true
            }
        }

        findPreference<SwitchPreferenceCompat>(Constants.PREF_HAPTIC_FEEDBACK)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    VibrationUtils.performHapticFeedback(view)
                }

                true
            }
        }

        findPreference<Preference>(Constants.PREF_BACKUP_DB)?.apply {
            setOnPreferenceClickListener {
                VibrationUtils.performHapticFeedback(view)
                lifecycleScope.launch {
                    val data = Repository.getAll()
                    val jsonData = gson.toJson(data)
                    backupBinding = DialogBackupBinding.inflate(layoutInflater)
                    backupBinding.jsonOutput.text = jsonData

                    activity?.let {
                        MaterialAlertDialogBuilder(it)
                            .setTitle(R.string.export_data)
                            .setView(backupBinding.root)
                            .setPositiveButton(R.string.ok) { _, _ ->
                                VibrationUtils.performHapticFeedback(view)
                            }
                            .show()
                    }
                }
                true
            }
        }

        findPreference<Preference>(Constants.PREF_RESTORE_DB)?.apply {
            setOnPreferenceClickListener {
                VibrationUtils.performHapticFeedback(view)


                restoreBinding = DialogRestoreBinding.inflate(layoutInflater)
                activity?.let { it1 ->
                    val dialog = MaterialAlertDialogBuilder(it1)
                        .setTitle(R.string.restore_data)
                        .setView(restoreBinding.root)
                        .setPositiveButton(R.string.ok, null)
                        .setNegativeButton(R.string.cancel, null)
                        .show()

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        VibrationUtils.performHapticFeedback(view)

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
                                        VibrationUtils.performHapticFeedback(view)

                                        restartApp(context)
                                    }
                                    .setNegativeButton(R.string.cancel, null)
                                    .setCancelable(false)
                                    .show()
                            } catch (e: JsonSyntaxException) {
                                restoreBinding.jsonInput.error =
                                    getString(R.string.json_data_incorrect)
                            } catch (e: Exception) {
                                if (GlobalValues.devMode) {
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

        findPreference<Preference>(Constants.PREF_ALL_TASKS)?.apply {
            setOnPreferenceClickListener {
                startActivity(Intent(context, AllTasksActivity::class.java))
                view?.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                true
            }
        }

        findPreference<Preference>(Constants.PREF_ABOUT)?.apply {
            setOnPreferenceClickListener {
                startActivity(Intent(context, AboutActivity::class.java))
                view?.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)

                true
            }
        }
    }


    override fun setDivider(divider: Drawable?) {
        super.setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setDividerHeight(height: Int) {
        super.setDividerHeight(0)
    }


    private fun restartApp(restartContext: Context) {
        val intent = Intent(restartContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        restartContext.startActivity(intent)
        exitProcess(0)
    }
}