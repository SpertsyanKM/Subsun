package ru.lefty.subsun

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.lefty.subsun.ui.subscription.Subscription
import ru.lefty.subsun.ui.subscriptionList.SubscriptionList

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.SubscriptionList.route) {
        composable(Screen.SubscriptionList.route) { SubscriptionList(navController) }
        composable(Screen.Subscription.route) { Subscription() }
    }
}
