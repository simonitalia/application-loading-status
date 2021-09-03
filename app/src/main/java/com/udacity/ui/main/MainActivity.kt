package com.udacity.ui.main

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import com.udacity.R
import com.udacity.support.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var urlString = ""

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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
        custom_button.setOnClickListener {
            download(urlString)
        }
    }

    // on receiver to trigger notification
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val downloadCompletedText = context?.getText(R.string.download_completed)
            Log.i("MainActivity.onReceive", "$downloadCompletedText.")
            Toast.makeText(this@MainActivity, downloadCompletedText, Toast.LENGTH_SHORT).show()

            // when download completes
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(urlString: String) {

         if (urlString.isNullOrEmpty()) {
             val message = this.getText(R.string.select_radio)
             Snackbar.make(custom_button, message, Snackbar.LENGTH_LONG).show()
             return
         }

        val fullUrlString = Constants.GH_BASE_URL+urlString
        Log.i("MainActivity.download", "Downloading project from url: $fullUrlString.")

        val request =
            DownloadManager.Request(Uri.parse(fullUrlString))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
