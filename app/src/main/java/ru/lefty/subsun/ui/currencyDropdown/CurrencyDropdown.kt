package ru.lefty.subsun.ui.currencyDropdown

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import ru.lefty.subsun.model.Currency

@Composable
fun CurrencyDropdown(
    isExpanded: Boolean,
    onDismissed: () -> Unit,
    onCurrencyChosen: (Currency) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onDismissed() }
    ) {
        Currency.values().forEach {
            DropdownMenuItem(onClick = { onCurrencyChosen(it) }) {
                Text(
                    text = it.value,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
