package ru.lefty.subsun.ui.model

import androidx.annotation.StringRes
import ru.lefty.subsun.R

enum class SortingOrder(@StringRes val nameId: Int) {
    CREATION_DATE(R.string.creation_date),
    TITLE(R.string.subscription_title),
    PRICE(R.string.subscription_price),
    NEAREST_PAYMENT(R.string.nearest_payment),
    EXHAUSTION(R.string.exhaustion)
}
