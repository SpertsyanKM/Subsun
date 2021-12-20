package ru.lefty.subsun.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.lefty.subsun.data.subscription.SubscriptionsDao
import ru.lefty.subsun.model.Currency
import java.lang.NumberFormatException
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.ui.NAV_PARAM_SUBSCRIPTION_ID_DEFAULT

data class SubscriptionViewModelState constructor(
    val title: String = "",
    val description: String = "",
    val priceString: String = "",
    val currency: Currency = Currency.Dollar,
    val isTitleError: Boolean = false,
    val isPriceError: Boolean = false,
    val isDescriptionError: Boolean = false
)

class SubscriptionViewModel(
    private val subscriptionsDao: SubscriptionsDao,
    private val navController: NavHostController,
    subscriptionId: Long?
): ViewModel() {
    private val viewModelState = MutableStateFlow(SubscriptionViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )
    private var editingSubscription: Subscription? = null

    init {
        subscriptionId?.let { id ->
            viewModelScope.launch {
                subscriptionsDao.getById(id)?.let { subscription ->
                    editingSubscription = subscription
                    viewModelState.update { it.copy(
                        title = subscription.title,
                        description = subscription.description,
                        priceString = subscription.price.toString(),
                        currency = subscription.currency
                    ) }
                }
            }
        }
    }

    fun onTitleChanged(newTitle: String) {
        viewModelState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChanged(newDescription: String) {
        viewModelState.update { it.copy(description = newDescription) }
    }

    fun onPriceChanged(newPrice: String) {
        try {
            newPrice.toFloat()
            viewModelState.update { it.copy(priceString = newPrice) }
        } catch (_: NumberFormatException) {
            // Ignore change
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            editingSubscription?.let {
                updateEditingSubscription(it.id)
            } ?: saveNewSubscription()

            launch(Dispatchers.Main) {
                navController.popBackStack()
            }
        }
    }

    private suspend fun saveNewSubscription() {
        val newSubscription = Subscription(
            viewModelState.value.title,
            viewModelState.value.description,
            viewModelState.value.priceString.toFloat(),
            viewModelState.value.currency,
        )
        subscriptionsDao.insert(newSubscription)
    }

    private suspend fun updateEditingSubscription(id: Long) {
        val newSubscription = Subscription(
            viewModelState.value.title,
            viewModelState.value.description,
            viewModelState.value.priceString.toFloat(),
            viewModelState.value.currency,
            id = id
        )
        subscriptionsDao.update(newSubscription)
    }

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
            navController: NavHostController,
            subscriptionId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionViewModel(
                    subscriptionsDao,
                    navController,
                    subscriptionId.takeIf { it != NAV_PARAM_SUBSCRIPTION_ID_DEFAULT }
                ) as T
            }
        }
    }
}
