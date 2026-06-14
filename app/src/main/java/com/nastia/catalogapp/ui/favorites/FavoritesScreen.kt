package com.nastia.catalogapp.ui.favorites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FavoritesScreen(
    onProductClick: (Int) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Text("Favorites")
    }
}