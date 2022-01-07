package ru.lefty.subsun.model

import androidx.room.TypeConverter

enum class Currency(val value: String, val code: String, val beforeAmount: Boolean) {
    Dollar("$", "USD", true),
    Rouble("₽", "RUB", false),
    Euro("€", "EUR", false);

    class Converters {
        @TypeConverter
        fun fromString(code: String?) = values().find { it.code == code }

        @TypeConverter
        fun toString(currency: Currency?) = currency?.code
    }
}