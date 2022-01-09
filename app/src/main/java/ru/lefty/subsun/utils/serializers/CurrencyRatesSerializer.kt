package ru.lefty.subsun.utils.serializers

import org.json.JSONObject
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.CurrencyRates

private const val KEY_TIMESTAMP = "timestamp"
private const val KEY_RATES = "rates"

class CurrencyRatesSerializer : Serializer<CurrencyRates> {

    override fun toJson(value: CurrencyRates): String {
        val currencyConverters = Currency.Converters()
        val json = JSONObject()
        json.put(KEY_TIMESTAMP, value.lastUpdatedTimestamp)

        val ratesJson = JSONObject()
        value.rates.forEach { (currency, nestedRates) ->
            val nestedRatesJson = JSONObject()
            nestedRates.forEach { (nestedCurrency, rate) ->
                nestedRatesJson.put(currencyConverters.toString(nestedCurrency), rate)
            }
            ratesJson.put(currencyConverters.toString(currency), nestedRates)
        }
        json.put(KEY_RATES, ratesJson)

        return json.toString()
    }

    override fun fromJson(json: String): CurrencyRates {
        val currencyConverters = Currency.Converters()
        val jsonObject = JSONObject(json)
        val ratesJson = jsonObject.getJSONObject(KEY_RATES)
        val ratesMap = mutableMapOf<Currency, MutableMap<Currency, Float>>()
        ratesJson.keys().forEach { currencyCode ->
            val currency = currencyConverters.fromString(currencyCode) ?: return@forEach
            val nestedRatesMap = mutableMapOf<Currency, Float>()

            val nestedRatesJson = ratesJson.getJSONObject(currencyCode)
            nestedRatesJson.keys().forEach nestedForEach@ { nestedCurrencyCode ->
                val nestedCurrency = currencyConverters.fromString(nestedCurrencyCode) ?: return@nestedForEach
                val rate = nestedRatesJson.getDouble(nestedCurrencyCode).toFloat()
                nestedRatesMap[nestedCurrency] = rate
            }

            ratesMap[currency] = nestedRatesMap
        }

        val timestamp = jsonObject.getLong(KEY_TIMESTAMP)
        return CurrencyRates(ratesMap, timestamp)
    }
}