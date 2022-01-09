package ru.lefty.subsun.services.currencyExchanger

import ru.lefty.subsun.model.CurrencyRates

interface CurrencyExchangerApi {

    suspend fun loadCurrencyRates(): CurrencyRates
}