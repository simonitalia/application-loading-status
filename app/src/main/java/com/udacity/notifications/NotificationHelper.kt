package com.udacity.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.support.Constants
import com.udacity.ui.detail.DetailActivity
import com.udacity.ui.detail.DetailActivity.Companion.PROJECT_DOWNLOAD_SUCCESS_FLAG

object NotificationHelper {

    /**
     * Sets up the notification channels for API 26+.
     * Note: This uses package name + channel name to create unique channelId's.
     *
     * @param context     application context
     * @param channelId  notificaiton channel Id
     * @param channelName   name for the notification channel
     */
    fun createChannel(context: Context, channelId: String, channelName: String) {

        // create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH // notification importance

            ).apply {
                setShowBadge(true)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = context.getString(R.string.notification_message)

            // register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}

/**
 * Notification Extension methods
 * Builds and delivers the notification.
 * @param context, activity context.
 */

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
    downloadId: Long?, //for future use
    isDownloadSuccessful: Boolean,
    messageBody: String,
    projectUrlString: String,
    applicationContext: Context
) {

    // create the content intent for the notification
    // this launches Detail Activity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(DetailActivity.PROJECT_URL_STRING, projectUrlString)
    contentIntent.putExtra(PROJECT_DOWNLOAD_SUCCESS_FLAG, isDownloadSuccessful)

    // create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT // create a new intent or use an existing intent (in this case use existing)
    )

    // get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        Constants.CHANNEL_ID //assign teh channel to this notification
    )

        // set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)

        // pass pending intent to notification
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        // set content intent
        .setSmallIcon(R.drawable.check_circle_24dp)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true) // for the notification to dismiss itself once user taps and is taken to the app

        // set notification button and action
        .addAction(
            R.drawable.file_download_24dp,
            applicationContext.getString(R.string.notification_action_button),
            contentPendingIntent //trigger pending intent
        )

    // call notify on Notification Compat builder
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}