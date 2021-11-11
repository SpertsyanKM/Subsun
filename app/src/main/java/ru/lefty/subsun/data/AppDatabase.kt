package ru.lefty.subsun.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lefty.subsun.data.subscription.SubscriptionsDao
import ru.lefty.subsun.model.Subscription

@Database(entities = [Subscription::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionsDao(): SubscriptionsDao
}
