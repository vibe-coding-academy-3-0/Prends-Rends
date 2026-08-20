package com.example.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val loanId = intent.getLongExtra(EXTRA_LOAN_ID, -1L)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Rappel de prêt"
        val contactName = intent.getStringExtra(EXTRA_CONTACT_NAME) ?: "Un proche"
        val isLent = intent.getBooleanExtra(EXTRA_IS_LENT, true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Ensure channel exists
        NotificationHelper.createNotificationChannel(context)

        // Content intent to open MainActivity
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("LOAN_ID", loanId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            loanId.toInt(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message = if (isLent) {
            "C'est le jour d'échéance ! Recontactez $contactName pour récupérer : $title"
        } else {
            "C'est le jour d'échéance ! N'oubliez pas de rendre \"$title\" à $contactName"
        }

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("⏰ Rappel : $title")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        notificationManager.notify(loanId.toInt().coerceAtLeast(1000), notification)
    }

    companion object {
        const val EXTRA_LOAN_ID = "extra_loan_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_CONTACT_NAME = "extra_contact_name"
        const val EXTRA_IS_LENT = "extra_is_lent"
    }
}
