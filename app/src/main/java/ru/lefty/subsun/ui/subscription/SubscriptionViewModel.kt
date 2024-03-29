package ru.lefty.subsun.ui.subscription

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lefty.subsun.data.dao.SettingsDao
import ru.lefty.subsun.data.dao.SubscriptionsDao
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.PeriodicityInterval
import java.lang.NumberFormatException
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.services.notificationSender.NotificationSender
import ru.lefty.subsun.ui.NAV_PARAM_SUBSCRIPTION_ID_DEFAULT
import java.util.*

data class SubscriptionViewModelState(
    val title: String = "",
    val description: String = "",
    val priceString: String = "",
    val currency: Currency = Currency.Dollar,
    val periodCountString: String = "1",
    val periodicityInterval: PeriodicityInterval = PeriodicityInterval.MONTH,
    val firstPaymentDate: Date = Date(),
    val remindDaysAgoString: String = "1",
    val shouldRemind: Boolean = false,
    val isTitleError: Boolean = false,
    val isPriceError: Boolean = false,
    val isPeriodCountError: Boolean = false,
    val isPeriodicityDropdownExpanded: Boolean = false,
    val isCurrencyDropdownExpanded: Boolean = false,
    val isNewSubscription: Boolean = true,
)

class SubscriptionViewModel(
    private val subscriptionsDao: SubscriptionsDao,
    private val settingsDao: SettingsDao,
    private val navController: NavHostController,
    private val notificationSender: NotificationSender,
    application: Application,
    subscriptionId: Long?
): AndroidViewModel(application) {
    private val viewModelState = MutableStateFlow(SubscriptionViewModelState())
    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )
    private var editingSubscription: Subscription? = null

    init {
        viewModelScope.launch {
            subscriptionId?.let { id ->
                subscriptionsDao.getById(id)?.let { subscription ->
                    editingSubscription = subscription
                    viewModelState.update { it.copy(
                        title = subscription.title,
                        description = subscription.description,
                        priceString = subscription.price.toString(),
                        currency = subscription.currency,
                        periodCountString = subscription.periodCount.toString(),
                        periodicityInterval = subscription.periodicityInterval,
                        firstPaymentDate = subscription.firstPaymentDate,
                        shouldRemind = subscription.shouldRemind,
                        remindDaysAgoString = subscription.remindDaysAgo.takeIf { subscription.shouldRemind }?.toString() ?: "1",
                        isNewSubscription = false,
                    ) }
                }
            } ?: settingsDao.get().collect { settings ->
                settings?.let {
                    viewModelState.update { it.copy(currency = settings.currency) }
                }
            }
        }
    }

    fun onBackClicked() {
        navController.popBackStack()
    }

    fun onNotificationsClicked() {

    }

    fun onDeleteConfirmed() {
        editingSubscription?.let {
            viewModelScope.launch {
                subscriptionsDao.delete(it)
                withContext(Dispatchers.Main) {
                    navController.popBackStack()
                }
            }
        }
    }

    fun onTitleChanged(newTitle: String) {
        viewModelState.update { it.copy(
            title = newTitle,
            isTitleError = false
        ) }
    }

    fun onDescriptionChanged(newDescription: String) {
        viewModelState.update { it.copy(description = newDescription) }
    }

    fun onPeriodCountChanged(newPeriodCount: String) {
        try {
            newPeriodCount.takeIf { it.isNotEmpty() }?.toInt()
            viewModelState.update { it.copy(
                periodCountString = newPeriodCount,
                isPeriodCountError = false
            ) }
        } catch (_: NumberFormatException) {
            // Ignore change
        }
    }

    fun onRemindDaysAgoChanged(newRemindDaysAgo: String) {
        try {
            newRemindDaysAgo.takeIf { it.isNotEmpty() }?.toInt()
            viewModelState.update { it.copy(
                remindDaysAgoString = newRemindDaysAgo,
            ) }
        } catch (_: NumberFormatException) {
            // Ignore change
        }
    }

    fun onShouldRemindChanged(newShouldRemind: Boolean) {
        viewModelState.update { it.copy(
            shouldRemind = newShouldRemind
        ) }
    }

    fun onPriceChanged(newPrice: String) {
        try {
            newPrice.takeIf { it.isNotEmpty() }?.toFloat()
            viewModelState.update { it.copy(
                priceString = newPrice,
                isPriceError = false
            ) }
        } catch (_: NumberFormatException) {
            // Ignore change
        }
    }

    fun onPeriodicityIntervalButtonClicked() {
        viewModelState.update { it.copy(isPeriodicityDropdownExpanded = true) }
    }

    fun onPeriodicityIntervalDropdownDismissed() {
        viewModelState.update { it.copy(isPeriodicityDropdownExpanded = false) }
    }

    fun onPeriodicityIntervalChanged(newPeriodicityInterval: PeriodicityInterval) {
        viewModelState.update { it.copy(
            periodicityInterval = newPeriodicityInterval,
            isPeriodicityDropdownExpanded = false
        ) }
    }

    fun onFirstPaymentDateChanged(date: Date) {
        viewModelState.update { it.copy(
            firstPaymentDate = date
        ) }
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

    fun onSaveClicked() {
        val isTitleError = viewModelState.value.title.isBlank()
        val isPriceError = viewModelState.value.priceString.isEmpty()
        val isPeriodCountError = viewModelState.value.periodCountString.isEmpty()

        val hasError = isTitleError || isPriceError || isPeriodCountError

        if (hasError) {
            viewModelState.update { it.copy(
                isTitleError = isTitleError,
                isPriceError = isPriceError,
                isPeriodCountError = isPeriodCountError
            ) }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                editingSubscription?.let {
                    updateEditingSubscription(it)
                } ?: saveNewSubscription()

                launch(Dispatchers.Main) {
                    navController.popBackStack()
                }
            }
        }
    }

    private suspend fun saveNewSubscription() {
        val newSubscription = prepareSubscription()
        subscriptionsDao.insert(newSubscription)

        notificationSender.sendScheduled(getApplication<Application>(), newSubscription)
    }

    private suspend fun updateEditingSubscription(editingSubscription: Subscription) {
        val newSubscription = prepareSubscription(
            editingSubscription.id,
            editingSubscription.creationDate)
        subscriptionsDao.update(newSubscription)

        notificationSender.cancelScheduled(getApplication<Application>(), editingSubscription)
        notificationSender.sendScheduled(getApplication<Application>(), newSubscription)
    }

    private fun prepareSubscription(id: Long = 0, creationDate: Date = Date()): Subscription {
        val remindDaysAgo = if (viewModelState.value.shouldRemind) {
            viewModelState.value.remindDaysAgoString.toInt()
        } else {
            -1
        }

        return Subscription(
            viewModelState.value.title,
            viewModelState.value.description,
            viewModelState.value.priceString.toFloat(),
            viewModelState.value.currency,
            viewModelState.value.periodCountString.toInt(),
            viewModelState.value.periodicityInterval,
            viewModelState.value.firstPaymentDate,
            remindDaysAgo,
            creationDate = creationDate,
            id = id
        )
    }

    companion object {
        fun provideFactory(
            subscriptionsDao: SubscriptionsDao,
            settingsDao: SettingsDao,
            navController: NavHostController,
            notificationSender: NotificationSender,
            application: Application,
            subscriptionId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubscriptionViewModel(
                    subscriptionsDao,
                    settingsDao,
                    navController,
                    notificationSender,
                    application,
                    subscriptionId.takeIf { it != NAV_PARAM_SUBSCRIPTION_ID_DEFAULT }
                ) as T
            }
        }
    }
}
