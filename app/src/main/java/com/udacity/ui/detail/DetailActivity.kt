package com.udacity.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.support.Constants
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity: AppCompatActivity() {

    // key for passing downloaded project url string via intent
    companion object {
        const val PROJECT_URL_STRING = "PROJECT_URL_STRING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val projectUrl = intent.getStringExtra(PROJECT_URL_STRING)

        project_name_text_view.text = (when (projectUrl) {

            Constants.GLIDE_REPO_URL -> {
                "Glide Project"
            }

            Constants.ADVANCED_PROGRAMMING_REPO_URL -> {
                "Android Advanced Programming Project"
            }

            Constants.RETROFIT_REPO_URL -> {
                "Retrofit Project"
            }

            else -> {
                "UNKNOWN_PROJECT"
            }
        })

        ok_button.setOnClickListener {
            onBackPressed()
        }
    }
}
