package ru.lefty.subsun.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lefty.subsun.utils.MS_IN_DAY
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
    val remindDaysAgo: Int = 0,
    val creationDate: Date = Date(),
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    private val intervalDays get() = periodCount * periodicityInterval.averageDayCount

    val shouldRemind get() = remindDaysAgo >= 0

    val daysFromLastPayment: Float get() {
        val diff = (Date().time - firstPaymentDate.time) / MS_IN_DAY
        return if (diff <= 0) {
            0f
        } else {
            (diff % intervalDays)
        }
    }

    val daysTillNextPayment get() = intervalDays - daysFromLastPayment

    val progressTillNextPayment get() = daysFromLastPayment / intervalDays
}
