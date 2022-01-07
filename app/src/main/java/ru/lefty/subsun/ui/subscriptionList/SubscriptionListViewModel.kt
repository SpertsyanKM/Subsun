package ru.lefty.subsun.ui.subscriptionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lefty.subsun.data.dao.SubscriptionsDao
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.ui.Screen
import ru.lefty.subsun.data.Preferences
import ru.lefty.subsun.data.dao.SettingsDao
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.PeriodicityInterval
import ru.lefty.subsun.ui.NAV_PARAM_SUBSCRIPTION_ID
import ru.lefty.subsun.ui.model.SortingOrder
import ru.lefty.subsun.ui.round

data class SubscriptionListViewModelState(
    val isLoading: Boolean = false,
    val subscriptions: Set<Subscription> = emptySet(),
    val currentCurrency: Currency = Currency.Dollar,
    val periodicityInterval: PeriodicityInterval = PeriodicityInterval.MONTH,
    val sortingOrder: SortingOrder = SortingOrder.CREATION_DATE
) {
    val totalPrice get() = subscriptions.sumOf {
        it.periodicityInterval.getPriceForInterval(it.price, periodicityInterval).toDouble()
    }.toFloat().round(2)

    val sortedSubscriptions get() = when (sortingOrder) {
        SortingOrder.CREATION_DATE -> subscriptions.sortedBy { it.creationDate }
        SortingOrder.TITLE -> subscriptions.sortedBy { it.title }
        SortingOrder.PRICE -> subscriptions.sortedBy { it.price }
        SortingOrder.NEAREST_PAYMENT -> subscriptions.sortedBy { it.daysTillNextPayment }
        SortingOrder.EXHAUSTION -> subscriptions.sortedByDescending { it.progressTillNextPayment }
    }
}

@ExperimentalCoroutinesApi
class SubscriptionListViewModel(
    private val subscriptionsDao: SubscriptionsDao,
    private val settingsDao: SettingsDao,
    private val preferences: Preferences,
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
            val periodicityInterval = preferences.getString(Preferences.Key.PERIODICITY_INTERVAL)?.let {
                PeriodicityInterval.Converters().fromString(it)
            } ?: PeriodicityInterval.MONTH

            val sortingOrder = preferences.getString(Preferences.Key.SORTING_ORDER)?.let {
                SortingOrder.fromCode(it)
            } ?: SortingOrder.CREATION_DATE

            subscriptionsDao.getAll().combine(settingsDao.get()) {
                subscriptions, settings -> subscriptions to settings
            }.collect { (subscriptions, settings) ->
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        subscriptions = subscriptions.toSet(),
                        periodicityInterval = periodicityInterval,
                        sortingOrder = sortingOrder,
                        currentCurrency = settings?.currency ?: Currency.Dollar
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

    fun onSettingsClicked() {
        navController.navigate(Screen.Settings.route)
    }

    fun onPeriodicityIntervalClick() {
        val intervals = PeriodicityInterval.values()
        val currentPeriodicityIntervalIndex =
            intervals.indexOf(viewModelState.value.periodicityInterval)
        val newPeriodicityInterval = intervals[(currentPeriodicityIntervalIndex + 1) % intervals.size]
        PeriodicityInterval.Converters().toString(newPeriodicityInterval)?.let {
            preferences.putString(Preferences.Key.PERIODICITY_INTERVAL, it)
        }
        viewModelState.update { it.copy(periodicityInterval = newPeriodicityInterval) }
    }

    fun onSortingOrderChanged(newSortingOrder: SortingOrder) {
        preferences.putString(Preferences.Key.SORTING_ORDER, newSortingOrder.code)
        viewModelState.update { it.copy(sortingOrder = newSortingOrder) }
    }

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
            settingsDao: SettingsDao,
            preferences: Preferences,
            navController: NavHostController
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionListViewModel(
                    subscriptionsDao,
                    settingsDao,
                    preferences,
                    navController
                ) as T
            }
        }
    }
}