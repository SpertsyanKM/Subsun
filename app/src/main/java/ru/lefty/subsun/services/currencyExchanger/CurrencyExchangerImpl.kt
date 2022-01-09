package ru.lefty.subsun.services.currencyExchanger

import android.util.Log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lefty.subsun.data.Preferences
import ru.lefty.subsun.model.Currency
import ru.lefty.subsun.model.CurrencyRates
import ru.lefty.subsun.utils.MS_IN_DAY
import ru.lefty.subsun.utils.serializers.Serializer
import java.lang.Exception
import java.util.*
import kotlin.IllegalStateException

private const val TAG = "CurrencyExchangerImpl"

class CurrencyExchangerImpl(
    private val api: CurrencyExchangerApi,
    private val preferences: Preferences,
    private val serializer: Serializer<CurrencyRates>
): CurrencyExchanger {

    private var rates: CurrencyRates? = null
    private var isInitialized = false
    private var initializationDeferred: CompletableDeferred<Unit>? = null

    init {
        initializationDeferred = CompletableDeferred()

        val storedRates = try {
            preferences.getString(Preferences.Key.CURRENCY_RATES)?.let {
                serializer.fromJson(it)
            }
        } catch (e: Exception) {
            null
        }

        val isActual = storedRates?.let {
            val actualTs = Date().time - MS_IN_DAY * 7
            it.lastUpdatedTimestamp >= actualTs
        } == true

        if (!isActual) {
            loadRates()
        }
    }

    override val isReady get() = isInitialized

    override suspend fun awaitForInit() {
        initializationDeferred?.await()
    }

    override fun lastUpdatedTimestamp() = rates?.lastUpdatedTimestamp

    override fun convert(value: Float, from: Currency, to: Currency): Float {
        if (from == to) {
            return value
        }

        rates?.let {
            it.rates[from]?.get(to)?.let { rate ->
                return value * rate
            } ?: throw IllegalStateException("Exchange rate for given currencies not found")
        } ?: throw IllegalStateException("Exchange rates were not loaded")
    }

    private fun loadRates() {
        CoroutineScope(Dispatchers.IO).launch {
            rates = try {
                api.loadCurrencyRates()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load rates", e)
                null
            }
            rates?.let { storeRates(it) }
            finishInitialization()
        }
    }

    private fun finishInitialization() {
        isInitialized = true
        initializationDeferred?.complete(Unit)
    }

    private fun storeRates(rates: CurrencyRates) {
        try {
            serializer.toJson(rates).let {
                preferences.putString(Preferences.Key.CURRENCY_RATES, it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to store rates", e)
        }
    }
}