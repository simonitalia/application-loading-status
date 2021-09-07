package com.udacity.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.support.Constants
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity: AppCompatActivity() {

    // keys for passing values via intent
    companion object {
        const val PROJECT_URL_STRING = "PROJECT_URL_STRING"
        const val PROJECT_DOWNLOAD_SUCCESS_FLAG = "PROJECT_DOWNLOAD_SUCCESS_FLAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val projectUrl = intent.getStringExtra(PROJECT_URL_STRING)
        val downloadSuccessful = intent.getBooleanExtra(PROJECT_DOWNLOAD_SUCCESS_FLAG, false)

        project_name_text_view.text = (when (projectUrl) {

            Constants.GLIDE_REPO_URL -> {
                getString(R.string.glide_project_name)
            }

            Constants.ADVANCED_PROGRAMMING_REPO_URL -> {
                getString(R.string.android_advanced_programming_project_name)
            }

            Constants.RETROFIT_REPO_URL -> {
                getString(R.string.retrofit_project_name)
            }

            else -> {
                getString(R.string.unknown_project_name)
            }
        })

        download_status_text_view.text = when (downloadSuccessful) {
            true -> getString(R.string.download_success_message)
            false -> getString(R.string.download_failed_message)
        }

        ok_button.setOnClickListener {
            onBackPressed()
        }
    }
}
