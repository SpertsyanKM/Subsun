package ru.lefty.subsun.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lefty.subsun.data.dao.SettingsDao
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.Settings as SettingsModel

data class SettingsViewModelState(
    val currency: Currency = Currency.Dollar,
    val isCurrencyDropdownExpanded: Boolean = false
)

class SettingsViewModel(
    private val settingsDao: SettingsDao,
    private val navController: NavHostController
): ViewModel() {

    private val viewModelState = MutableStateFlow(SettingsViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    init {
        viewModelScope.launch {
            settingsDao.get().collect { settings ->
                viewModelState.update {
                    it.copy(currency = settings?.currency ?: Currency.Dollar)
                }
            }
        }
    }

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
        viewModelScope.launch { settingsDao.insert(SettingsModel(newCurrency)) }
        viewModelState.update { it.copy(
            isCurrencyDropdownExpanded = false,
            currency = newCurrency
        ) }
    }

    companion object {
        fun provideFactory(
            settingsDao: SettingsDao,
            navController: NavHostController,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(
                    settingsDao,
                    navController
                ) as T
            }
        }
    }
}