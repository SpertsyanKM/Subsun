package ru.lefty.subsun.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Subscription(
    @PrimaryKey
    val id: Long
)
