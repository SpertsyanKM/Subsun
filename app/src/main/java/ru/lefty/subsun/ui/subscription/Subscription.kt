package ru.lefty.subsun.ui.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.lefty.subsun.R

private const val PRICE_BOX_HEIGHT = 220
private const val CONTENT_BOX_HORIZONTAL_PADDING = 20
private const val CONTENT_BOX_TOP_PADDING = 100
private const val PRICE_BOX_TOP_PADDING = CONTENT_BOX_TOP_PADDING / 2
private const val CONTENT_BOX_INNER_PADDING = PRICE_BOX_HEIGHT - CONTENT_BOX_TOP_PADDING + 16

@Composable
fun Subscription(
    viewModel: SubscriptionViewModel
) {
    Box(
        modifier = Modifier
            .padding(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = dimensionResource(R.dimen.padding_xxl)
            )
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    CONTENT_BOX_HORIZONTAL_PADDING.dp,
                    CONTENT_BOX_TOP_PADDING.dp,
                    CONTENT_BOX_HORIZONTAL_PADDING.dp,
                    dimensionResource(id = R.dimen.padding_xxl)
                )
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_m)))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            colorResource(id = R.color.gradient_blue_1),
                            colorResource(id = R.color.gradient_blue_2)
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            RestSubscriptionContent(viewModel)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PRICE_BOX_HEIGHT.dp)
                .padding(
                    (CONTENT_BOX_HORIZONTAL_PADDING * 2).dp,
                    PRICE_BOX_TOP_PADDING.dp,
                    (CONTENT_BOX_HORIZONTAL_PADDING * 2).dp,
                    0.dp
                )
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_m)))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            colorResource(id = R.color.gradient_pink_1),
                            colorResource(id = R.color.gradient_pink_2)
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
        ) {
            PriceBoxContent(viewModel)
        }
        Box(
            Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_l))
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { viewModel.onSaveClicked() },
                content = {
                    Text(
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.padding_xxxl),
                            dimensionResource(id = R.dimen.padding_s),
                        ),
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.h6
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.button_blue),
                    contentColor = colorResource(id = R.color.white)
                )
            )
        }
    }
}

@Composable
fun PriceBoxContent(
    viewModel: SubscriptionViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val priceFocusRequester = remember { FocusRequester() }
    val whiteColor = colorResource(id = R.color.white)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        TextField(
            value = uiState.value.priceString,
            onValueChange = { newPrice -> viewModel.onPriceChanged(newPrice) },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = whiteColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                color = whiteColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.focusRequester(priceFocusRequester)
        )
        Divider(
            color = whiteColor,
            thickness = 1.dp,
            modifier = Modifier
                .width(100.dp)
        )
        Text(
            text = uiState.value.currency.value,
            style = MaterialTheme.typography.h5,
            color = whiteColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                0.dp,
                dimensionResource(id = R.dimen.padding_s),
                0.dp,
                0.dp
            )
        )
    }

    DisposableEffect(Unit) {
        priceFocusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun RestSubscriptionContent(
    viewModel: SubscriptionViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val whiteColor = colorResource(id = R.color.light_white)

    Column(
        modifier = Modifier.padding(
            dimensionResource(id = R.dimen.padding_m),
            CONTENT_BOX_INNER_PADDING.dp,
            dimensionResource(id = R.dimen.padding_m),
            0.dp
        )
    ) {
        OutlinedTextField(
            value = uiState.value.title,
            label = { Text(stringResource(R.string.subscription_title)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = whiteColor,
                unfocusedIndicatorColor = whiteColor,
                cursorColor = whiteColor,
                focusedLabelColor = whiteColor,
                unfocusedLabelColor = whiteColor
            ),
            textStyle = LocalTextStyle.current.copy(
                color = whiteColor,
            ),
            onValueChange = { newTitle -> viewModel.onTitleChanged(newTitle) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.value.description,
            label = { Text(stringResource(R.string.subscription_description)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = whiteColor,
                unfocusedIndicatorColor = whiteColor,
                cursorColor = whiteColor,
                focusedLabelColor = whiteColor,
                unfocusedLabelColor = whiteColor
            ),
            textStyle = LocalTextStyle.current.copy(
                color = whiteColor,
            ),
            onValueChange = { newDescription -> viewModel.onDescriptionChanged(newDescription) },
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
    }
}