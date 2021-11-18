package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import ru.lefty.subsun.R
import ru.lefty.subsun.utils.toPriceString
import ru.lefty.subsun.model.Subscription

@Composable
fun SubscriptionCard(subscription: Subscription, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable { onClick() }.padding(0.dp, dimensionResource(id = R.dimen.padding_s))
    ) {
        Column {
            Text(text = subscription.title)
            Text(text = subscription.description)
        }
        Text(text = subscription.price.toPriceString())
        Text(text = subscription.currency.value)
    }
}