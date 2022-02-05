package ru.lefty.subsun.model

import androidx.annotation.StringRes
import androidx.room.TypeConverter
import ru.lefty.subsun.R
import java.util.Date
import java.util.Calendar

enum class PeriodicityInterval(
    @StringRes val nameKey: Int,
    @StringRes val singularFormKey: Int,
    @StringRes val pluralFormKey: Int,
    val code: String,
    val averageDayCount: Float,
    private val calendarField: Int
) {
    DAY(R.string.daily, R.string.day, R.string.days, "daily", 1f, Calendar.DATE),
    WEEK(R.string.weekly, R.string.week, R.string.weeks, "weekly", 7f, Calendar.WEEK_OF_YEAR),
    MONTH(R.string.monthly, R.string.month, R.string.months, "monthly", 30.42f, Calendar.MONTH),
    YEAR(R.string.yearly, R.string.year, R.string.years, "yearly", 365.25f, Calendar.YEAR);

    @StringRes fun getCorrectFormForCount(count: Int): Int {
        return if (count == 1) {
            singularFormKey
        } else {
            pluralFormKey
        }
    }

    fun getPriceForInterval(currentPrice: Float, requiredInterval: PeriodicityInterval): Float {
        val pricePerDay = currentPrice / averageDayCount
        return pricePerDay * requiredInterval.averageDayCount
    }

    fun getPreviousDate(date: Date, periodCount: Int): Date {
        val today = Date()
        if (today.time < date.time) {
            return date
        }

        val futureDate = getFutureDate(date, periodCount)

        val calendar = Calendar.getInstance()
        calendar.time = futureDate
        calendar.add(calendarField, -periodCount)

        return calendar.time
    }

    fun getFutureDate(date: Date, periodCount: Int): Date {
        val today = Date()
        if (today.time < date.time) {
            return date
        }

        val calendar = Calendar.getInstance()
        calendar.time = date

        while (today.time > calendar.time.time) {
            calendar.add(calendarField, periodCount)
        }

        return calendar.time
    }

    class Converters {
        @TypeConverter
        fun fromString(code: String?) = values().find { it.code == code }

        @TypeConverter
        fun toString(interval: PeriodicityInterval?) = interval?.code
    }
}
