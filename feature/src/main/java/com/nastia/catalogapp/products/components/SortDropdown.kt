package com.nastia.catalogapp.products.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nastia.catalogapp.R
import com.nastia.catalogapp.model.ProductSort

@Composable
fun SortDropdown(
    selected: ProductSort,
    onSortSelected: (ProductSort) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            AssistChip(
                onClick = { expanded = true },
                label = { Text(stringResource(R.string.products_sort_prefix, selected.label())) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ProductSort.entries.forEach { sort ->
                    DropdownMenuItem(
                        text = { Text(sort.label()) },
                        onClick = {
                            onSortSelected(sort)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductSort.label(): String = stringResource(
    when (this) {
        ProductSort.DEFAULT -> R.string.sort_default
        ProductSort.PRICE_ASC -> R.string.sort_price_asc
        ProductSort.PRICE_DESC -> R.string.sort_price_desc
        ProductSort.RATING_DESC -> R.string.sort_rating_desc
        ProductSort.NAME_ASC -> R.string.sort_name_asc
    }
)
