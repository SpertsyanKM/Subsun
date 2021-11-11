package ru.lefty.subsun.di

import android.content.Context
import androidx.room.Room
import ru.lefty.subsun.data.AppDatabase
import ru.lefty.subsun.data.subscription.SubscriptionsDao

interface AppContainer {

    val appDatabase: AppDatabase
    val subscriptionsDao: SubscriptionsDao
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
}