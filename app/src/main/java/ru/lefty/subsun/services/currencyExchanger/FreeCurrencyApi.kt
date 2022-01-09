package ru.lefty.subsun.services.currencyExchanger

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.CurrencyRates
import ru.lefty.subsun.services.networkService.NetworkService
import java.util.*

private const val API_KEY = "5f932270-7141-11ec-abb6-1f85add173d4"
private const val API_URL = "https://freecurrencyapi.net/api/v2/latest?apikey=$API_KEY"

class FreeCurrencyApi(private val networkService: NetworkService) : CurrencyExchangerApi {

    override suspend fun loadCurrencyRates(): CurrencyRates {
        return coroutineScope {
            val requests = Currency.values().map {
                val url = "$API_URL&base_currency=${it.code}"
                async {
                    it to networkService.get(url)
                }
            }
            val rateResponses = awaitAll(*requests.toTypedArray())
            val rates = mutableMapOf<Currency, Map<Currency, Float>>()
            rateResponses.forEach { (currency, response) ->
                rates[currency] = parseRates(response)
            }

            CurrencyRates(rates, Date().time)
        }
    }

    private fun parseRates(responseJson: String): Map<Currency, Float> {
        val json = JSONObject(responseJson)
        val data = json.getJSONObject("data")
        val result = mutableMapOf<Currency, Float>()
        Currency.values().forEach { currency ->
            data
                .takeIf { it.has(currency.code) }
                ?.getDouble(currency.code)
                ?.let { rate -> result[currency] = rate.toFloat() }
        }

        return result
    }
}