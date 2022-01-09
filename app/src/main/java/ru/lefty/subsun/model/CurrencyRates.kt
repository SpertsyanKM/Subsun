package ru.lefty.subsun.model

class CurrencyRates(
    val rates: Map<Currency, Map<Currency, Float>>,
    val lastUpdatedTimestamp: Long
)