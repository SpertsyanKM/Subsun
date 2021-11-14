package ru.lefty.subsun.ui.subscription

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import ru.lefty.subsun.R

@Composable
fun Subscription(
    viewModel: SubscriptionViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Text(text = "Create subscription")
    OutlinedTextField(
        value = uiState.value.title,
        label = { Text(stringResource(R.string.subscription_title)) },
        onValueChange = { newTitle -> viewModel.onTitleChanged(newTitle) }
    )
}
