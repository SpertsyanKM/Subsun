package ru.lefty.subsun.data

import android.content.SharedPreferences

class Preferences(private val sharedPreferences: SharedPreferences) {

    fun getString(key: Key, defValue: String? = null) =
        sharedPreferences.getString(key.value, defValue)

    fun putString(key: Key, value: String) =
        sharedPreferences.edit().putString(key.value, value).commit()

    enum class Key(internal val value: String) {
        SORTING_ORDER("sorting_order"),
        PERIODICITY_INTERVAL("periodicity_interval"),
        CURRENCY_RATES("currency_rates")
    }
}