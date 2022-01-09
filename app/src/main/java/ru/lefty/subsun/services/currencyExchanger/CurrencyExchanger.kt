package ru.lefty.subsun.services.currencyExchanger

import ru.lefty.subsun.model.Currency
import kotlin.jvm.Throws

interface CurrencyExchanger {

    val isReady: Boolean

    suspend fun awaitForInit()

    fun lastUpdatedTimestamp(): Long?

    @Throws(IllegalStateException::class)
    fun convert(value: Float, from: Currency, to: Currency): Float
}
