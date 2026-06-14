package com.nastia.catalogapp.ui.products

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onAddProduct: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Text("Product List", modifier = Modifier)
    }
}