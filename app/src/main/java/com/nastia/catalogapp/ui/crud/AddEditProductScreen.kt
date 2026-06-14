package com.nastia.catalogapp.ui.crud

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddEditProductScreen(
    productId: Int?,
    onNavigateUp: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Text(if (productId != null) "Edit Product $productId" else "Add Product")
    }
}