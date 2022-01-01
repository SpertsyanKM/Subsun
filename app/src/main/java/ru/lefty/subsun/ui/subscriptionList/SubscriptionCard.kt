package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import ru.lefty.subsun.R
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.utils.getPriceString

@ExperimentalMaterialApi
@Composable
fun SubscriptionCard(subscription: Subscription, onClick: () -> Unit) {
    Box(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_m))) {
            Column(modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)) {
                Text(text = subscription.title, style = MaterialTheme.typography.h6)
                if (subscription.description.isNotBlank()) {
                    Text(text = subscription.description, style = MaterialTheme.typography.body2)
                }
            }
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Row {
                    Text(
                        text = getPriceString(subscription.price, subscription.currency),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}