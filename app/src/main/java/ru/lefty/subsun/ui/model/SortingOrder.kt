package ru.lefty.subsun.ui.model

import androidx.annotation.StringRes
import ru.lefty.subsun.R

enum class SortingOrder(@StringRes val nameId: Int, val code: String) {
    CREATION_DATE(R.string.creation_date, "creation_date"),
    TITLE(R.string.subscription_title, "title"),
    PRICE(R.string.subscription_price, "price"),
    NEAREST_PAYMENT(R.string.nearest_payment, "nearest_payment"),
    EXHAUSTION(R.string.exhaustion, "exhaustion");

    companion object {
        fun fromCode(code: String) = values().find { it.code == code }
    }
}
