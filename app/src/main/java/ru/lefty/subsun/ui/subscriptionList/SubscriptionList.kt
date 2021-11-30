package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.lefty.subsun.R

@ExperimentalMaterialApi
@Composable
fun SubscriptionList(viewModel: SubscriptionListViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val subscriptionList = uiState.value.subscriptions

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddClick() },
            ) {
                Icon(Icons.Outlined.Add,"Add")
            }
        }
    ) {
        if (subscriptionList.isEmpty()) {
            EmptySubscriptionList()
        } else {
            Column {
                subscriptionList.map { subscription -> SubscriptionCard(subscription) {
                    viewModel.onSubscriptionClick(
                        subscription
                    )
                } }
            }
        }
    }
}

@Composable
fun EmptySubscriptionList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.Subscriptions,
            contentDescription = "Subscriptions",
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )

        Text(
            text = stringResource(R.string.subscriptions_empty_state),
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.padding_xl),
                vertical = dimensionResource(id = R.dimen.padding_xs)
            )
        )
    }
}