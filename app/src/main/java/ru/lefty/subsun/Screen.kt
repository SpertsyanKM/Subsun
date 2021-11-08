package ru.lefty.subsun

import androidx.annotation.StringRes

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object SubscriptionList : Screen("subscriptionList", R.string.subscription_list)
    object Subscription : Screen("subscription", R.string.subscription)
}
