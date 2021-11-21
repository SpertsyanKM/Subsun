package ru.lefty.subsun.ui.subscriptionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.lefty.subsun.data.subscription.SubscriptionsDao
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.ui.Screen
import kotlinx.coroutines.flow.collect
import ru.lefty.subsun.ui.NAV_PARAM_SUBSCRIPTION_ID

data class SubscriptionListViewModelState(
    val isLoading: Boolean = false,
    val subscriptions: Set<Subscription> = emptySet()
)

class SubscriptionListViewModel(
    private val subscriptionsDao: SubscriptionsDao,
    private val navController: NavHostController
): ViewModel() {
    private val viewModelState = MutableStateFlow(SubscriptionListViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    init {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            subscriptionsDao.getAll().collect { subscriptions ->
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        subscriptions = subscriptions.toSet()
                    )
                }
            }
        }
    }

    fun onSubscriptionClick(subscription: Subscription) {
        navController.navigate("${Screen.Subscription.route}?$NAV_PARAM_SUBSCRIPTION_ID=${subscription.id}")
    }

    fun onAddClick() {
        navController.navigate(Screen.Subscription.route)
    }

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
            navController: NavHostController,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionListViewModel(subscriptionsDao, navController) as T
            }
        }
    }
}