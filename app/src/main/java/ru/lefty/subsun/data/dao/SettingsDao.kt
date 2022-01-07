package ru.lefty.subsun.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.lefty.subsun.model.Settings
import ru.lefty.subsun.model.Subscription

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun get(): Flow<Settings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: Settings)
}