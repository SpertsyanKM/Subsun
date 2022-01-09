package ru.lefty.subsun.ui.subscriptionList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.lefty.subsun.R
import ru.lefty.subsun.ui.bottomBar.SubsunBottomBar
import ru.lefty.subsun.utils.getPriceString

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun SubscriptionList(viewModel: SubscriptionListViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val subscriptionList = uiState.value.sortedSubscriptions
    val fabShape = RoundedCornerShape(50)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddClick() },
                shape = fabShape
            ) {
                Icon(Icons.Outlined.Add,"Add")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            SubsunBottomBar(
                fabShape,
                uiState.value.sortingOrder,
                { viewModel.onSortingOrderChanged(it) },
                { viewModel.onSettingsClicked() }
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_gray))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding_m))
                ) {
                    Text(
                        text = stringResource(id = R.string.subscriptions),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.weight(1f)
                    )
                    Column(
                        modifier = Modifier
                            .clickable {
                                viewModel.onPeriodicityIntervalClick()
                            }
                            .padding(
                                vertical = dimensionResource(id = R.dimen.padding_xs),
                                horizontal = dimensionResource(id = R.dimen.padding_m)
                            )
                    ) {
                        Text(
                            text = stringResource(
                                id = uiState.value.periodicityInterval.nameKey
                            ),
                            style = MaterialTheme.typography.caption
                        )
                        val totalPrice = viewModel.totalPrice
                        if (uiState.value.isLoadingExchangeRates || totalPrice == null) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = dimensionResource(id = R.dimen.padding_xs)),
                                strokeWidth = 1.dp
                            )
                        } else {
                            Text(
                                text = getPriceString(
                                    totalPrice,
                                    uiState.value.currentCurrency
                                ),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
                if (subscriptionList.isEmpty()) {
                    EmptySubscriptionList()
                } else {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_s)))
                            .padding(dimensionResource(id = R.dimen.padding_m), 0.dp)
                            .background(Color.White),
                    ) {
                        subscriptionList.mapIndexed { index, subscription ->
                            SubscriptionCard(subscription) {
                                viewModel.onSubscriptionClick(
                                    subscription
                                )
                            }
                            if (index + 1 < subscriptionList.count()) {
                                Divider(
                                    color = colorResource(id = R.color.background_gray),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(dimensionResource(id = R.dimen.padding_l), 0.dp)
                                        .height(1.dp)
                                )
                            }
                        }
                    }
                }
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