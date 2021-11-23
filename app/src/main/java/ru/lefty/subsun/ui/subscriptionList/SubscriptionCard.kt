package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import ru.lefty.subsun.R
import ru.lefty.subsun.utils.toPriceString
import ru.lefty.subsun.model.Subscription

@ExperimentalMaterialApi
@Composable
fun SubscriptionCard(subscription: Subscription, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_s), dimensionResource(id = R.dimen.padding_xs))
    ) {
        Card(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_s)),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() }
        ) {
            Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_s))) {
                Column(modifier = Modifier.weight(1f).align(Alignment.CenterVertically)) {
                    Text(text = subscription.title)
                    if (subscription.description.isNotBlank()) {
                        Text(text = subscription.description)
                    }
                }
                Column {
                    Row {
                        Text(text = subscription.price.toPriceString())
                        Text(text = subscription.currency.value)
                    }
                }
            }
        }
    }
}