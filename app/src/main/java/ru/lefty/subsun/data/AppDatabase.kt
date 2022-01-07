package ru.lefty.subsun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.lefty.subsun.data.dao.SettingsDao
import ru.lefty.subsun.data.dao.SubscriptionsDao
import ru.lefty.subsun.model.*
import ru.lefty.subsun.utils.DateConverters

@Database(entities = [
    Subscription::class,
    Settings:: class
], version = 1)
@TypeConverters(
    Currency.Converters::class,
    PeriodicityInterval.Converters::class,
    DateConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionsDao(): SubscriptionsDao

    abstract fun settingsDao(): SettingsDao
}
