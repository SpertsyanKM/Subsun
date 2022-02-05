package ru.lefty.subsun.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lefty.subsun.utils.MS_IN_DAY
import java.util.Date

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
    val shouldRemind get() = remindDaysAgo >= 0

    val lastPaymentDay get() = periodicityInterval.getPreviousDate(firstPaymentDate, periodCount)

    val nextPaymentDate get() = periodicityInterval.getFutureDate(firstPaymentDate, periodCount)

    val daysFromLastPayment: Float get() {
        val diff = (Date().time - lastPaymentDay.time) / MS_IN_DAY
        return if (diff < 0) 0f else diff.toFloat()
    }

    val daysTillNextPayment: Float get() {
        val diff = (nextPaymentDate.time - Date().time) / MS_IN_DAY
        return if (diff < 0) 0f else diff.toFloat()
    }

    val progressTillNextPayment get() = daysFromLastPayment / (daysFromLastPayment + daysTillNextPayment)
}
