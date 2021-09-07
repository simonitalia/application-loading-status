package com.udacity.notifications

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.udacity.R

open class DownloadReceiver: BroadcastReceiver() {

    var projectUrl = ""

    override fun onReceive(context: Context, intent: Intent?) {

        // when download completes

        // get downloadId (set with download request in MainActivity)
        val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        val downloadCompletedText = context.getText(R.string.notification_message)
        Log.i("DownloadReceiver.onReceive", "$downloadCompletedText")

        // deliver notification to system
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        // cancel all existing notifications
        notificationManager.cancelNotifications()

        notificationManager.sendNotification(
            downloadId,
            downloadCompletedText.toString(),
            projectUrl,
            context
        )
    }
}