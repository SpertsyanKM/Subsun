package ru.lefty.subsun.services.notificationSender

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.lefty.subsun.model.Subscription
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.lefty.subsun.R
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build


private const val EX_SUBSCRIPTION_ID = "subscription_id"
private const val EX_SUBSCRIPTION_TITLE = "subscription_title"
private const val EX_REMIND_DAYS_AGO = "remind_days_ago"

class NotificationSenderImpl: NotificationSender, BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == null) {
            showNotification(context, intent)
        } else {
            //
        }
    }

    override fun sendScheduled(context: Context, subscription: Subscription) {
        val intent = prepareIntent(context, subscription)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 5000, 60000, intent)
    }

    override fun cancelScheduled(context: Context, subscription: Subscription) {
        val intent = prepareIntent(context, subscription)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(intent)
    }

    private fun prepareIntent(context: Context, subscription: Subscription): PendingIntent {
        val intent = Intent(context, NotificationSenderImpl::class.java)
        intent.putExtra(EX_SUBSCRIPTION_ID, subscription.id)
        intent.putExtra(EX_SUBSCRIPTION_TITLE, subscription.title)
        intent.putExtra(EX_REMIND_DAYS_AGO, subscription.remindDaysAgo)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun showNotification(context: Context, intent: Intent) {
        val subscriptionId = intent.getLongExtra(EX_SUBSCRIPTION_ID, 0)
        val remindDaysAgo = intent.getIntExtra(EX_REMIND_DAYS_AGO, 0)
        val subscriptionTitle = intent.getStringExtra(EX_SUBSCRIPTION_TITLE) ?: "<unknown>"
        val expires = when (remindDaysAgo) {
            0 -> context.getString(R.string.today)
            1 -> context.getString(R.string.tomorrow)
            else -> context.getString(R.string.in_n_days, remindDaysAgo.toString())
        }

        val title = context.getString(R.string.subscription_expires_soon)
        val text = context.getString(R.string.notification_text, subscriptionTitle, expires)

        val notificationIntent = Intent(Intent.ACTION_VIEW)
        notificationIntent.data = Uri.parse("subsun://subsun.ru/subscription/$subscriptionId")
        val pendingIntent = PendingIntent.getActivity(
            context,0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "notifications"
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                createNotificationChannel(channel)
                notificationBuilder.setChannelId(channelId)
            }

            // notificationId is a unique int for each notification that you must define
            notify(subscriptionId.toInt(), notificationBuilder.build())
        }
    }
}