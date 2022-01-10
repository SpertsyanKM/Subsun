package ru.lefty.subsun.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.lefty.subsun.model.Subscription

@Dao
interface SubscriptionsDao {
    @Query("SELECT * FROM subscription")
    fun getAll(): Flow<List<Subscription>>

    @Query("SELECT * FROM subscription WHERE id = :id")
    suspend fun getById(id: Long): Subscription?

    @Insert
    suspend fun insert(subscription: Subscription)

    @Update
    suspend fun update(subscription: Subscription)

    @Delete
    suspend fun delete(subscription: Subscription)
}