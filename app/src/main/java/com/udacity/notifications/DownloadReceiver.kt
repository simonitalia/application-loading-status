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
    var downloadManager: DownloadManager? = null
    private var isDownloadSuccessful = false

    override fun onReceive(context: Context, intent: Intent?) {

        // when download completes

        // get downloadId (set with download request in MainActivity)
        val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)?.also { id ->

            val query = DownloadManager.Query().setFilterById(id)

            downloadManager?.query(query)?.let { cursor ->

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> isDownloadSuccessful = true
                        DownloadManager.STATUS_FAILED -> isDownloadSuccessful = false
                    }
                }
            }
        }

        val downloadCompletedText = context.getText(R.string.notification_message)
        Log.i("DownloadReceiver.onReceive", "$downloadCompletedText. Download successful: $isDownloadSuccessful")

        // deliver notification to system
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        // cancel all existing notifications
        notificationManager.cancelNotifications()

        notificationManager.sendNotification(
            downloadId,
            isDownloadSuccessful,
            downloadCompletedText.toString(),
            projectUrl,
            context
        )
    }
}