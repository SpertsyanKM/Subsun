package ru.lefty.subsun.di

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.Room
import ru.lefty.subsun.data.AppDatabase
import ru.lefty.subsun.data.Preferences
import ru.lefty.subsun.data.dao.SettingsDao
import ru.lefty.subsun.data.dao.SubscriptionsDao

interface AppContainer {

    val appDatabase: AppDatabase
    val subscriptionsDao: SubscriptionsDao
    val settingsDao: SettingsDao
    val preferences: Preferences
}

class AppContainerImpl(private val applicationContext: Context): AppContainer {

    override val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "SubsunDb"
        ).build()
    }

    override val subscriptionsDao: SubscriptionsDao get() = appDatabase.subscriptionsDao()
    override val settingsDao: SettingsDao get() = appDatabase.settingsDao()

    override val preferences: Preferences by lazy {
        Preferences(PreferenceManager.getDefaultSharedPreferences(applicationContext))
    }
}
