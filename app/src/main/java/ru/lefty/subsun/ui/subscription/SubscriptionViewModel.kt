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

data class SubscriptionViewModelState(
    val title: String = "",
    val description: String = "",
    val price: Float? = null,
    val currency: Currency = Currency.Dollar,
) {
    val priceAsString get() = price?.let {
        price.toString()
    }
}

class SubscriptionViewModel(
    private val subscriptionsDao: SubscriptionsDao,
    private val navController: NavHostController
): ViewModel() {
    private val viewModelState = MutableStateFlow(SubscriptionViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    fun onTitleChanged(newTitle: String) {
        viewModelState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChanged(newDescription: String) {
        viewModelState.update { it.copy(description = newDescription) }
    }

    fun onPriceChanged(newPrice: String) {
        try {
            val price =newPrice.toFloat()
            viewModelState.update { it.copy(price = price) }
        } catch (_: NumberFormatException) {
            // Ignore change
        }
    }

    fun onSaveClicked() {
        val newSubscription = Subscription(
            viewModelState.value.title,
            viewModelState.value.description,
            viewModelState.value.price ?: 0f,
            viewModelState.value.currency
        )
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionsDao.insert(newSubscription)

            launch(Dispatchers.Main) {
                navController.popBackStack()
            }
        }
    }

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
            navController: NavHostController,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionViewModel(subscriptionsDao, navController) as T
            }
        }
    }
}
