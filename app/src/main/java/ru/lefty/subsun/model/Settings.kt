package ru.lefty.subsun.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Settings(
    val currency: Currency,
    @PrimaryKey(autoGenerate = false)
    val id: Long = 1 // always one row
)
