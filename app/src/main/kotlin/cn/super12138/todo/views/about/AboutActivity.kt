package cn.super12138.todo.views.about

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import cn.super12138.todo.constant.Constants
import cn.super12138.todo.databinding.ActivityAboutBinding
import cn.super12138.todo.utils.VersionUtils
import cn.super12138.todo.utils.showToast
import cn.super12138.todo.views.BaseActivity

class AboutActivity : BaseActivity() {
    private lateinit var binding: ActivityAboutBinding
    private var clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appVersion.text = VersionUtils.getAppVersion(this)

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        binding.checkUpdate.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constants.UPDATE_URL)
            }
            startActivity(intent)
        }

        binding.openSource.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(Constants.REPO_GITHUB_URL)
            }
            startActivity(intent)
        }


        binding.developerInfo.setOnClickListener {
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
                    "\uD83C\uDF3C".showToast()
                }
            }
        }
    }
}