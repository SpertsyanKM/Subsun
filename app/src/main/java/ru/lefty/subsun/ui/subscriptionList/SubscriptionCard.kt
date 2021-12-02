package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import ru.lefty.subsun.R
import ru.lefty.subsun.utils.toPriceString
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.ui.theme.SubsunTheme

@ExperimentalMaterialApi
@Composable
fun SubscriptionCard(subscription: Subscription, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_m), 6.dp)
    ) {
        Card(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_s)),
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() }
        ) {
            Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_m))) {
                Column(modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)) {
                    Text(text = subscription.title, style = MaterialTheme.typography.h5)
                    if (subscription.description.isNotBlank()) {
                        Text(text = subscription.description, style = MaterialTheme.typography.caption)
                    }
                }
                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Row {
                        Text(text = subscription.price.toPriceString())
                        Text(text = subscription.currency.value)
                    }
                }
            }
        }
    }
}