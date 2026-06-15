package com.nastia.catalogapp.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LanguageOption(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    RadioButton(selected = selected, onClick = onClick)
    Text(label, modifier = Modifier.padding(end = 24.dp))
}