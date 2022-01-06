package ru.lefty.subsun.model

import androidx.annotation.StringRes
import androidx.room.TypeConverter
import ru.lefty.subsun.R

enum class PeriodicityInterval(
    @StringRes val nameKey: Int,
    @StringRes val singularFormKey: Int,
    @StringRes val pluralFormKey: Int,
    val code: String,
    val averageDayCount: Float
) {
    DAY(R.string.daily, R.string.day, R.string.days, "daily", 1f),
    WEEK(R.string.weekly, R.string.week, R.string.weeks, "weekly", 7f),
    MONTH(R.string.monthly, R.string.month, R.string.months, "monthly", 30.42f),
    YEAR(R.string.yearly, R.string.year, R.string.years, "yearly", 365.25f);

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

    class Converters {
        @TypeConverter
        fun fromString(code: String?) = values().find { it.code == code }

        @TypeConverter
        fun toString(interval: PeriodicityInterval?) = interval?.code
    }
}
