package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.lefty.subsun.R

@Composable
fun SubscriptionList() {
    val subscriptionList = emptyList<Unit>()

    if (subscriptionList.isEmpty()) {
        EmptySubscriptionList()
    }
}

@Composable
fun EmptySubscriptionList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Outlined.Subscriptions, contentDescription = "", modifier = Modifier
            .width(50.dp)
            .height(50.dp))
        Text(text = stringResource(R.string.subscriptions_empty_state))
    }
}