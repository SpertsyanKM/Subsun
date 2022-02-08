package ru.lefty.subsun.ui.subscription

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.lefty.subsun.R
import ru.lefty.subsun.model.PeriodicityInterval
import ru.lefty.subsun.ui.clickableWithoutRipple
import ru.lefty.subsun.ui.currencyDropdown.CurrencyDropdown
import ru.lefty.subsun.ui.showDatePicker
import java.text.DateFormat

private const val PRICE_BOX_HEIGHT = 180

@Composable
fun Subscription(viewModel: SubscriptionViewModel) {
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val uiState = viewModel.uiState.collectAsState()

    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { viewModel.onBackClicked() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            actions = {
                if (!uiState.value.isNewSubscription) {
                    IconButton(onClick = { showDeleteConfirmationDialog.value = true }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }
                }
            }
        )
        Content(viewModel)
        if (showDeleteConfirmationDialog.value) {
            ConfirmationDialog(
                onDismiss = { showDeleteConfirmationDialog.value = false },
                onConfirm = {
                    showDeleteConfirmationDialog.value = false;
                    viewModel.onDeleteConfirmed()
                }
            )
        }
    }
}

@Composable
fun ConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.confirm_deleting_subscription)) },
        buttons = {
            Row(
                modifier = Modifier.padding(
                    all = dimensionResource(id = R.dimen.padding_l)
                ),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onConfirm() }
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            }
        }
    )
}

@Composable
fun Content(viewModel: SubscriptionViewModel) {
    Box {
        Column(
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.padding_m),
                    end = dimensionResource(id = R.dimen.padding_m)
                )
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PRICE_BOX_HEIGHT.dp)
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_m)
                    )
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.padding_m)))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                colorResource(id = R.color.gradient_dark_1),
                                colorResource(id = R.color.gradient_dark_2)
                            ),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
                    .border(
                        1.dp,
                        colorResource(id = R.color.secondary_variant),
                        RoundedCornerShape(dimensionResource(id = R.dimen.padding_m))
                    )
            ) {
                PriceBoxContent(viewModel)
            }
            RestSubscriptionContent(viewModel)
            Spacer(modifier = Modifier.height(100.dp))
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
fun PriceBoxContent(viewModel: SubscriptionViewModel) {
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
            isError = uiState.value.isPriceError,
            modifier = Modifier.focusRequester(priceFocusRequester)
        )
        Divider(
            color = whiteColor,
            thickness = 1.dp,
            modifier = Modifier
                .width(100.dp)
        )
        Box(modifier = Modifier
            .clickable { viewModel.onCurrencyClicked() }
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_l),
                vertical = dimensionResource(id = R.dimen.padding_s)
            )
        ) {
            Text(
                text = uiState.value.currency.value,
                style = MaterialTheme.typography.h5,
                color = whiteColor,
                fontWeight = FontWeight.Bold,
            )
            CurrencyDropdown(
                isExpanded = uiState.value.isCurrencyDropdownExpanded,
                onDismissed = { viewModel.onCurrencyDropdownDismissed() },
                onCurrencyChosen = { viewModel.onCurrencyChanged(it) }
            )
        }
    }

    DisposableEffect(Unit) {
        priceFocusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun RestSubscriptionContent(viewModel: SubscriptionViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val textFieldColor = colorResource(id = R.color.primary)
    val periodicityIntervalNameStringId = uiState.value.periodicityInterval.getCorrectFormForCount(
        uiState.value.periodCountString.toIntOrNull() ?: 0
    )
    val context = LocalContext.current

    Column {
        ColoredTextField(
            value = uiState.value.title,
            label = { Text(stringResource(R.string.subscription_title)) },
            onValueChange = { newTitle -> viewModel.onTitleChanged(newTitle) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            color = textFieldColor,
            isError = uiState.value.isTitleError
        )
        ColoredTextField(
            value = uiState.value.description,
            label = { Text(stringResource(R.string.subscription_description)) },
            onValueChange = { newDescription -> viewModel.onDescriptionChanged(newDescription) },
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
            color = textFieldColor
        )
        Column(Modifier.padding(top = dimensionResource(id = R.dimen.padding_m))) {
            Text(
                text = stringResource(id = R.string.periodicity),
                style = MaterialTheme.typography.h6
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.every),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_m)),
                    color = Color.Black
                )
                ColoredTextField(
                    value = uiState.value.periodCountString,
                    label = { Text(stringResource(R.string.count)) },
                    onValueChange = { newPeriodCount ->
                        viewModel.onPeriodCountChanged(
                            newPeriodCount
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    color = textFieldColor,
                    modifier = Modifier.weight(1f),
                    isError = uiState.value.isPeriodCountError
                )
                Button(
                    content = {
                        Text(
                            text = stringResource(id = periodicityIntervalNameStringId),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black
                        )
                        DropdownMenu(
                            expanded = uiState.value.isPeriodicityDropdownExpanded,
                            onDismissRequest = { viewModel.onPeriodicityIntervalDropdownDismissed() }
                        ) {
                            PeriodicityInterval.values().forEach { interval ->
                                val nameStringId = interval.getCorrectFormForCount(
                                    uiState.value.periodCountString.toIntOrNull() ?: 0
                                )

                                DropdownMenuItem(
                                    onClick = { viewModel.onPeriodicityIntervalChanged(interval) }
                                ) {
                                    Text(
                                        text = stringResource(id = nameStringId),
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        }
                    },
                    onClick = { viewModel.onPeriodicityIntervalButtonClicked() },
                    colors = ButtonDefaults.outlinedButtonColors(),
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_m))
                        .weight(0.7f)
                        .height(50.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_m))
            ) {
                Text(
                    text = stringResource(id = R.string.first_payment) + ":",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_m)),
                    color = Color.Black
                )
                ColoredTextField(
                    value = DateFormat.getDateInstance().format(uiState.value.firstPaymentDate),
                    onValueChange = { newPeriodCount ->
                        viewModel.onPeriodCountChanged(newPeriodCount)
                    },
                    color = textFieldColor,
                    modifier = Modifier
                        .weight(1f)
                        .clickableWithoutRipple {
                            showDatePicker(
                                context,
                                uiState.value.firstPaymentDate,
                            ) { date ->
                                viewModel.onFirstPaymentDateChanged(date)
                            }
                        },
                    enabled = false
                )
            }
        }

        Column(Modifier.padding(top = dimensionResource(id = R.dimen.padding_m))) {
            Text(
                text = stringResource(id = R.string.remind),
                style = MaterialTheme.typography.h6,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.value.shouldRemind,
                    onCheckedChange = { checked -> viewModel.onShouldRemindChanged(checked) }
                )
                ColoredTextField(
                    value = uiState.value.remindDaysAgoString,
                    label = { Text(stringResource(R.string.count)) },
                    onValueChange = { newRemindDaysAgo ->
                        viewModel.onRemindDaysAgoChanged(newRemindDaysAgo)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    color = textFieldColor,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = dimensionResource(id = R.dimen.padding_m)),
                    enabled = uiState.value.shouldRemind
                )
                Text(
                    text = stringResource(id = R.string.days_in_advance),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_m)),
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun ColoredTextField(
    value: String,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    color: Color,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        label = label,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = color,
            unfocusedIndicatorColor = color,
            cursorColor = color,
            focusedLabelColor = color,
            unfocusedLabelColor = color,
            disabledIndicatorColor = color,
        ),
        trailingIcon = {
            if (isError) {
                Icon(
                    Icons.Filled.Error,
                    stringResource(id = R.string.error),
                    tint = MaterialTheme.colors.error
                )
            }
        },
        textStyle = LocalTextStyle.current.copy(
            color = color,
        ),
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        modifier = modifier,
        enabled = enabled,
        isError = isError
    )
}
