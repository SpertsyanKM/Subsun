package ru.lefty.subsun.data.subscription

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.lefty.subsun.model.Subscription

@Dao
interface SubscriptionsDao {
    @Query("SELECT * FROM subscription")
    suspend fun getAll(): List<Subscription>

    @Insert
    suspend fun insert(subscription: Subscription)
}