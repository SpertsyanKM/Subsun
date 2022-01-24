package ru.lefty.subsun.services.notificationSender

import android.content.Context
import ru.lefty.subsun.model.Subscription

interface NotificationSender {

    fun sendScheduled(context: Context, subscription: Subscription)

    fun cancelScheduled(context: Context, subscription: Subscription)
}