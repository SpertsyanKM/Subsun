package ru.lefty.subsun.ui.bottomBar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.lefty.subsun.R
import ru.lefty.subsun.ui.model.SortingOrder

@Composable
fun SubsunBottomBar(
    cutoutShape: Shape?,
    chosenSortingOrder: SortingOrder,
    onSortingOrderChanged: (SortingOrder) -> Unit
) {
    var isSortingOrderPopupExpanded by remember { mutableStateOf(false) }

    BottomAppBar(
        cutoutShape = cutoutShape
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Localized description")
            }
        }
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = { isSortingOrderPopupExpanded = true }) {
            Icon(Icons.Filled.Sort, contentDescription = stringResource(id = R.string.sorting))
            DropdownMenu(
                expanded = isSortingOrderPopupExpanded,
                onDismissRequest = { isSortingOrderPopupExpanded = false }
            ) {
                SortingOrder.values().forEach { sortingOrder ->
                    DropdownMenuItem(onClick = {
                        isSortingOrderPopupExpanded = false
                        onSortingOrderChanged(sortingOrder)
                    }) {
                        val fontWeight = if (chosenSortingOrder == sortingOrder) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                        Text(
                            fontWeight = fontWeight,
                            text = stringResource(id = sortingOrder.nameId),
                            style = MaterialTheme.typography.body2,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
