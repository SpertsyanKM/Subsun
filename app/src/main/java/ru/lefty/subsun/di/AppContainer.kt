package ru.lefty.subsun.di

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.Room
import ru.lefty.subsun.data.AppDatabase
import ru.lefty.subsun.data.Preferences
import ru.lefty.subsun.data.dao.SettingsDao
import ru.lefty.subsun.data.dao.SubscriptionsDao
import ru.lefty.subsun.model.CurrencyRates
import ru.lefty.subsun.services.currencyExchanger.CurrencyExchanger
import ru.lefty.subsun.services.currencyExchanger.CurrencyExchangerApi
import ru.lefty.subsun.services.currencyExchanger.CurrencyExchangerImpl
import ru.lefty.subsun.services.currencyExchanger.FreeCurrencyApi
import ru.lefty.subsun.services.networkService.NetworkService
import ru.lefty.subsun.services.networkService.NetworkServiceImpl
import ru.lefty.subsun.services.notificationSender.NotificationSender
import ru.lefty.subsun.services.notificationSender.NotificationSenderImpl
import ru.lefty.subsun.utils.serializers.CurrencyRatesSerializer
import ru.lefty.subsun.utils.serializers.Serializer

interface AppContainer {

    val appDatabase: AppDatabase
    val subscriptionsDao: SubscriptionsDao
    val settingsDao: SettingsDao
    val preferences: Preferences
    val currencyExchanger: CurrencyExchanger
    val currencyExchangerApi: CurrencyExchangerApi
    val networkService: NetworkService
    val currencyRatesSerializer: Serializer<CurrencyRates>
    val notificationSender: NotificationSender
    val application: Application
}

class AppContainerImpl(private val app: Application): AppContainer {

    override val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "SubsunDb"
        ).build()
    }

    override val subscriptionsDao: SubscriptionsDao get() = appDatabase.subscriptionsDao()
    override val settingsDao: SettingsDao get() = appDatabase.settingsDao()

    override val preferences: Preferences by lazy {
        Preferences(PreferenceManager.getDefaultSharedPreferences(app))
    }

    override val currencyExchanger: CurrencyExchanger by lazy {
        CurrencyExchangerImpl(currencyExchangerApi, preferences, currencyRatesSerializer)
    }
    override val currencyExchangerApi: CurrencyExchangerApi by lazy {
        FreeCurrencyApi(networkService)
    }
    override val networkService: NetworkService by lazy {
        NetworkServiceImpl()
    }
    override val currencyRatesSerializer: Serializer<CurrencyRates> by lazy {
        CurrencyRatesSerializer()
    }

    override val notificationSender: NotificationSender by lazy {
        NotificationSenderImpl()
    }

    override val application: Application
        get() = app
}
