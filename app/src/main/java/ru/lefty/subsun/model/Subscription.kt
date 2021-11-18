package ru.lefty.subsun.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Subscription(
    val title: String,
    val description: String,
    val price: Float,
    val currency: Currency,
    val creationDate: Date = Date(),
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)
