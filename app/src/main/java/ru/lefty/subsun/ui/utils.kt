package ru.lefty.subsun.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import java.util.*

fun showDatePicker(
    context: Context,
    currentDate: Date,
    onDatePicked: (Date) -> Unit)
{
    val currentPaymentCalendar = Calendar.getInstance()
    currentPaymentCalendar.time = currentDate
    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            onDatePicked(calendar.time)
        },
        currentPaymentCalendar.get(Calendar.YEAR),
        currentPaymentCalendar.get(Calendar.MONTH),
        currentPaymentCalendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

inline fun Modifier.clickableWithoutRipple(crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}
