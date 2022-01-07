package ru.lefty.subsun.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.lefty.subsun.R

@Composable
fun SettingsItem(
    @StringRes labelId: Int,
    value: String,
    onClick: () -> Unit,
    valueContent: @Composable BoxScope.() -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(dimensionResource(id = R.dimen.padding_m))
    ) {
        Text(
            text = stringResource(id = labelId),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6
        )
        Box {
            Text(text = value, style = MaterialTheme.typography.body1)
            valueContent()
        }
    }
}
