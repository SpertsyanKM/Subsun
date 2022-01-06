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
    val periodCount: Int,
    val periodicityInterval: PeriodicityInterval,
    val firstPaymentDate: Date,
    val creationDate: Date = Date(),
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    private val intervalDays get() = periodCount * periodicityInterval.averageDayCount

    val daysFromLastPayment: Float get() {
        val diff = (Date().time - firstPaymentDate.time) / 1000 / 60 / 60 / 24
        return if (diff <= 0) {
            0f
        } else {
            (diff % intervalDays)
        }
    }

    val daysTillNextPayment get() = intervalDays - daysFromLastPayment

    val progressTillNextPayment get() = daysFromLastPayment / intervalDays
}
