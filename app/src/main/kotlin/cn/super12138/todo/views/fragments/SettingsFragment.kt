package cn.super12138.todo.views.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cn.super12138.todo.R
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.constant.GlobalValues
import cn.super12138.todo.logic.dao.ToDoRoomDB
import cn.super12138.todo.utils.VibrationUtils
import cn.super12138.todo.views.activities.MainActivity
import cn.super12138.todo.views.fragments.welcome.WelcomeFragment
import com.google.android.material.snackbar.Snackbar
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val mainActivity = requireActivity() as MainActivity
        val roomBackup = mainActivity.roomBackup

        findPreference<ListPreference>(Constants.PREF_DARK_MODE)?.apply {
            setOnPreferenceClickListener {
                VibrationUtils.performHapticFeedback(view)
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
                VibrationUtils.performHapticFeedback(view)


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
            setOnPreferenceChangeListener { _, _ ->
                VibrationUtils.performHapticFeedback(view)

                true
            }
        }

        findPreference<Preference>(Constants.PREF_BACKUP_DB)?.apply {
            setOnPreferenceClickListener {
                VibrationUtils.performHapticFeedback(view)
                val simpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                val formattedDate = simpleDateFormat.format(Date())
                roomBackup
                    .database(ToDoRoomDB.getDatabase(requireContext()))
                    .enableLogDebug(GlobalValues.devMode)
                    .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                    .customBackupFileName("ToDo-DataBase-$formattedDate.sqlite3")
                    .apply {
                        onCompleteListener { success, _, exitCode ->
                            if (success) {
                                view?.let { it1 ->
                                    Snackbar.make(
                                        it1, R.string.tips_backup_success, Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                view?.let { it1 ->
                                    Snackbar.make(
                                        it1, getString(
                                            R.string.tips_backup_failed,
                                            exitCode
                                        ), Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                    .backup()
                true
            }
        }

        findPreference<Preference>(Constants.PREF_RESTORE_DB)?.apply {
            setOnPreferenceClickListener {
                VibrationUtils.performHapticFeedback(view)

                roomBackup
                    .database(ToDoRoomDB.getDatabase(requireContext()))
                    .enableLogDebug(GlobalValues.devMode)
                    .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                    .apply {
                        onCompleteListener { success, _, exitCode ->
                            if (success) {
                                view?.let { it1 ->
                                    Snackbar.make(
                                        it1,
                                        R.string.tips_restore_success,
                                        Snackbar.LENGTH_LONG
                                    )
                                        .setAction(R.string.restart_app_now) {
                                            VibrationUtils.performHapticFeedback(view)
                                            restartApp(context)
                                        }
                                        .show()
                                }

                            } else {
                                view?.let { it1 ->
                                    Snackbar.make(
                                        it1, getString(
                                            R.string.tips_restore_failed,
                                            exitCode
                                        ), Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                    .restore()
                true
            }
        }

        findPreference<Preference>(Constants.PREF_ALL_TASKS)?.apply {
            setOnPreferenceClickListener {
                mainActivity.startFragment(AllTasksFragment())
                VibrationUtils.performHapticFeedback(view)
                true
            }
        }

        findPreference<Preference>(Constants.PREF_REENTER_WELCOME_ACTIVITY)?.apply {
            setOnPreferenceClickListener {
                mainActivity.startFragment(WelcomeFragment())
                VibrationUtils.performHapticFeedback(view)
                true
            }
        }

        findPreference<Preference>(Constants.PREF_ABOUT)?.apply {
            setOnPreferenceClickListener {
                mainActivity.startFragment(AboutFragment())
                VibrationUtils.performHapticFeedback(view)
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