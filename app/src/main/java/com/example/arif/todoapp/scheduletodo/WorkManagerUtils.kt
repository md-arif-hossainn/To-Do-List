package com.example.arif.todoapp.scheduletodo

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class WorkManagerUtils {
    fun schedule(context: Context, name: String, todoID: Long, delay: Long) {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("name" to name))
            .addTag("$name _ $todoID")
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}