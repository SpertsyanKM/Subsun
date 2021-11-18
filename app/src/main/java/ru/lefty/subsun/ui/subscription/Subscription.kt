package ru.lefty.subsun.ui.subscription

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import ru.lefty.subsun.R

@Composable
fun Subscription(
    viewModel: SubscriptionViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Column {
        Text(text = "Create subscription")
        OutlinedTextField(
            value = uiState.value.title,
            label = { Text(stringResource(R.string.subscription_title)) },
            onValueChange = { newTitle -> viewModel.onTitleChanged(newTitle) },
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.value.description,
            label = { Text(stringResource(R.string.subscription_description)) },
            onValueChange = { newDescription -> viewModel.onDescriptionChanged(newDescription) },
            maxLines = 5
        )
        Row {
            OutlinedTextField(
                value = uiState.value.priceAsString ?: "",
                label = { Text(stringResource(R.string.subscription_price)) },
                onValueChange = { newPrice -> viewModel.onPriceChanged(newPrice) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(text = "$")
        }
        Button(
            onClick = { viewModel.onSaveClicked() },
            content = {
                Text(text = "Save")
            }
        )
    }
}
