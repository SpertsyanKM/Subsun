package ru.lefty.subsun.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.lefty.subsun.di.AppContainer
import ru.lefty.subsun.ui.subscription.Subscription
import ru.lefty.subsun.ui.subscription.SubscriptionViewModel
import ru.lefty.subsun.ui.subscriptionList.SubscriptionList

@Composable
fun MainNavGraph(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.SubscriptionList.route) {
        composable(Screen.SubscriptionList.route) { SubscriptionList(navController) }
        composable(Screen.Subscription.route) {
            val subscriptionViewModel: SubscriptionViewModel = viewModel(
                factory = SubscriptionViewModel.provideFactory(appContainer.subscriptionsDao)
            )
            Subscription(subscriptionViewModel)
        }
    }
}