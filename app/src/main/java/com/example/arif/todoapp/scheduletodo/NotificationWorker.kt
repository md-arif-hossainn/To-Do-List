package com.example.arif.todoapp.scheduletodo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.arif.todoapp.R

class NotificationWorker(val context: Context, workParam: WorkerParameters)
    : Worker(context, workParam) {
    override fun doWork(): Result {
        val name = inputData.getString("name")
        sendNotification(name)
        return Result.success()
    }

    private fun sendNotification(name: String?) {
        val CHANNEL_ID = "my_channel"
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("Todo Alert!!!")
            .setContentText(name)
            .setVibrate(longArrayOf(500, 500, 1000, 1000, 500))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Schedule Notification Channel"
            val descriptionText = "This type of notification is sent to notify Todo alert"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).
            apply {
                description = descriptionText
            }
            // Register the channel with the system

            manager.createNotificationChannel(channel)
        }
        manager.notify(1, builder.build())
    }
}