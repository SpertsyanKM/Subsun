package ru.lefty.subsun.ui

import androidx.annotation.StringRes
import ru.lefty.subsun.R

sealed class Screen(val route: String, @StringRes val titleResourceId: Int) {
    object SubscriptionList : Screen("subscriptionList", R.string.subscription_list)
    object Subscription : Screen("subscription", R.string.subscription)
}
