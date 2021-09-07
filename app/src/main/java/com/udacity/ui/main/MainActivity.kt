package com.udacity.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.udacity.LoadingButton
import com.udacity.R
import com.udacity.notifications.DownloadReceiver
import com.udacity.notifications.NotificationHelper
import com.udacity.support.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var urlString = ""
    private var downloadID: Long = 0

    private lateinit var receiver: DownloadReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // create notification channel
        NotificationHelper.createChannel(applicationContext, Constants.CHANNEL_ID, Constants.CHANNEL_NOTIFICATION_NAME)

        // receiver to trigger notification
        receiver = object: DownloadReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                super.onReceive(context, intent)
                custom_loading_button.buttonState = LoadingButton.ButtonState.NORMAL
                Toast.makeText(context, context.getString(R.string.notification_message), Toast.LENGTH_SHORT).show()
            }
        }

        // register receiver
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // set repo download url based on radio button tapped
        radio_button_one.setOnClickListener {
            urlString = Constants.GLIDE_REPO_URL
        }

        radio_button_two.setOnClickListener {
            urlString = Constants.ADVANCED_PROGRAMMING_REPO_URL
        }

        radio_button_three.setOnClickListener {
            urlString = Constants.RETROFIT_REPO_URL
        }

        // custom download button tapped
        custom_loading_button.setOnClickListener {
            download(urlString)
            receiver.projectUrl = urlString
        }
    }

    //project download manager
    private fun download(urlString: String) {

         if (urlString.isNullOrEmpty()) {
             val message = this.getText(R.string.select_radio)
             Snackbar.make(custom_loading_button, message, Snackbar.LENGTH_LONG).show()
             return
         }

        // update custom loading button state
        custom_loading_button.buttonState = LoadingButton.ButtonState.DOWNLOADING

        val fullUrlString = Constants.GH_BASE_URL+urlString
        Log.i("MainActivity.download", "Downloading project from url: $fullUrlString. Please wait...")

        val request =
            DownloadManager.Request(Uri.parse(fullUrlString))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        receiver.downloadManager = downloadManager
        val id = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        downloadID = id

    }
}
