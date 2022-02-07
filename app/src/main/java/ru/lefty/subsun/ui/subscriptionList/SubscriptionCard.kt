package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.lefty.subsun.R
import ru.lefty.subsun.model.Subscription
import ru.lefty.subsun.ui.round
import ru.lefty.subsun.utils.getPriceString

@ExperimentalMaterialApi
@Composable
fun SubscriptionCard(subscription: Subscription, onClick: () -> Unit) {
    val padding = dimensionResource(id = R.dimen.padding_m)
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(padding)
    ) {
        Row(modifier = Modifier.padding(bottom = padding)) {
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
                    val priceString = getPriceString(subscription.price.round(2), subscription.currency)
                    val periodicity = stringResource(id = subscription.periodicityInterval.singularFormKey)
                    Text(
                        text = "$priceString / $periodicity",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
        LinearProgressIndicator(
            progress = subscription.progressTillNextPayment,
            Modifier.fillMaxWidth().height(2.dp)
        )
    }
}