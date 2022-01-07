package ru.lefty.subsun.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.lefty.subsun.data.subscription.SubscriptionsDao
import ru.lefty.subsun.model.Currency

data class SettingsViewModelState(
    val currency: Currency = Currency.Dollar,
    val isCurrencyDropdownExpanded: Boolean = false
)

class SettingsViewModel(
    private val subscriptionsDao: SubscriptionsDao,
    private val navController: NavHostController
): ViewModel() {

    fun onBackClicked() {
        navController.popBackStack()
    }

    fun onCurrencyClicked() {
        viewModelState.update { it.copy(
            isCurrencyDropdownExpanded = true
        ) }
    }

    fun onCurrencyDropdownDismissed() {
        viewModelState.update { it.copy(
            isCurrencyDropdownExpanded = false
        ) }
    }

    fun onCurrencyChanged(newCurrency: Currency) {
        viewModelState.update { it.copy(
            isCurrencyDropdownExpanded = false,
            currency = newCurrency
        ) }
    }

    private val viewModelState = MutableStateFlow(SettingsViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
            navController: NavHostController,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(
                    subscriptionsDao,
                    navController
                ) as T
            }
        }
    }
}