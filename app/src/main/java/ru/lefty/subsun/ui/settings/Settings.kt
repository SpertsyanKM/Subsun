package ru.lefty.subsun.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import ru.lefty.subsun.R
import ru.lefty.subsun.ui.currencyDropdown.CurrencyDropdown

@Composable
fun Settings(
    viewModel: SettingsViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Column {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.settings))
            },
            navigationIcon = {
                IconButton(onClick = { viewModel.onBackClicked() } ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
        SettingsItem(
            R.string.default_currency,
            uiState.value.currency.value,
            { viewModel.onCurrencyClicked() }
        ) {
            CurrencyDropdown(
                isExpanded = uiState.value.isCurrencyDropdownExpanded,
                onDismissed = { viewModel.onCurrencyDropdownDismissed() },
                onCurrencyChosen = { viewModel.onCurrencyChanged(it) }
            )
        }
    }
}