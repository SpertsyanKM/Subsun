package ru.lefty.subsun.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import ru.lefty.subsun.data.subscription.SubscriptionsDao
import ru.lefty.subsun.model.Currency
import java.util.*

data class SubscriptionViewModelState(
    val name: String = "",
    val description: String = "",
    val nextBillDate: Date? = null,
    val price: Float = 0f,
    val currency: Currency = Currency.Dollar,
)

class SubscriptionViewModel(
    private val subscriptionsDao: SubscriptionsDao
): ViewModel() {
    private val viewModelState = MutableStateFlow(SubscriptionViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionViewModel(subscriptionsDao) as T
            }
        }
    }
}
