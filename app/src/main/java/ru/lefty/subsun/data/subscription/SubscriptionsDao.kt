package ru.lefty.subsun.data.subscription

import androidx.room.Dao
import androidx.room.Query
import ru.lefty.subsun.model.Subscription

@Dao
interface SubscriptionsDao {
    @Query("SELECT * FROM subscription")
    fun getAll(): List<Subscription>
}