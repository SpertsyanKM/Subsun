package ru.lefty.subsun.utils

import ru.lefty.subsun.model.Currency

fun getPriceString(price: Float, currency: Currency): String {
    return if (currency.beforeAmount) {
        currency.value + price.toString()
    } else {
        price.toString() + ' ' + currency.value
    }
}
