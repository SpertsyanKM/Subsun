package ru.lefty.subsun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.lefty.subsun.data.subscription.SubscriptionsDao
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.Subscription

@Database(entities = [Subscription::class], version = 1)
@TypeConverters(Currency.Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionsDao(): SubscriptionsDao
}
