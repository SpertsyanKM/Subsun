package ru.lefty.subsun.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.lefty.subsun.data.Preferences
import ru.lefty.subsun.di.AppContainer
import ru.lefty.subsun.ui.settings.Settings
import ru.lefty.subsun.ui.settings.SettingsViewModel
import ru.lefty.subsun.ui.subscription.Subscription
import ru.lefty.subsun.ui.subscription.SubscriptionViewModel
import ru.lefty.subsun.ui.subscriptionList.SubscriptionList
import ru.lefty.subsun.ui.subscriptionList.SubscriptionListViewModel

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun MainNavGraph(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Screen.SubscriptionList.route) {
        composable(Screen.SubscriptionList.route) {
            val subscriptionListViewModel: SubscriptionListViewModel = viewModel(
                factory = SubscriptionListViewModel.provideFactory(
                    appContainer.subscriptionsDao,
                    appContainer.settingsDao,
                    appContainer.preferences,
                    navController,
                    appContainer.currencyExchanger
                )
            )
            SubscriptionList(subscriptionListViewModel)
        }
        composable(
            "${Screen.Subscription.route}?$NAV_PARAM_SUBSCRIPTION_ID={$NAV_PARAM_SUBSCRIPTION_ID}",
            arguments = listOf(navArgument(NAV_PARAM_SUBSCRIPTION_ID) {
                defaultValue = NAV_PARAM_SUBSCRIPTION_ID_DEFAULT
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val subscriptionViewModel: SubscriptionViewModel = viewModel(
                factory = SubscriptionViewModel.provideFactory(
                    appContainer.subscriptionsDao,
                    appContainer.settingsDao,
                    navController,
                    backStackEntry.arguments?.getLong(NAV_PARAM_SUBSCRIPTION_ID) ?: NAV_PARAM_SUBSCRIPTION_ID_DEFAULT
                )
            )
            Subscription(subscriptionViewModel)
        }
        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.provideFactory(
                    appContainer.settingsDao,
                    navController,
                )
            )

            Settings(settingsViewModel)
        }
    }
}
