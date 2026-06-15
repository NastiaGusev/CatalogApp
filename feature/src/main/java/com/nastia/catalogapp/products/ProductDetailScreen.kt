package com.nastia.catalogapp.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastia.catalogapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onNavigateUp: () -> Unit,
    onEditProduct: (Int) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (uiState as? ProductDetailUiState.Success)?.product?.title ?: ""
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back)
                        )
                    }
                },
                actions = {
                    if (uiState is ProductDetailUiState.Success) {
                        val state = uiState as ProductDetailUiState.Success
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = stringResource(R.string.detail_toggle_favorite),
                                tint = if (state.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { onEditProduct(productId) }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(R.string.detail_edit)
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is ProductDetailUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductDetailUiState.NotFound -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.product_not_found_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer()
                    Text(
                        stringResource(R.string.product_not_found_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer()
                    Button(onClick = onNavigateUp) {
                        Text(stringResource(R.string.product_not_found_go_back))
                    }
                }
            }

            is ProductDetailUiState.Success -> {
                val product = state.product

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
                ) {
                    item {
                        _root_ide_package_.com.nastia.catalogapp.products.components.ProductImageCarousel(
                            images = product.images.ifEmpty { listOf(product.thumbnail) })
                    }

                    item {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            Text(product.title, style = MaterialTheme.typography.headlineSmall)
                            Spacer()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "$${"%.2f".format(product.discountedPrice)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (product.discountPercentage > 0) {
                                    Text(
                                        text = "  $${"%.2f".format(product.price)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                }
                            }
                            Text(
                                "⭐ ${product.rating} · ${
                                    stringResource(
                                        R.string.products_in_stock,
                                        product.stock
                                    )
                                }", style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer()
                            Text(product.category, style = MaterialTheme.typography.labelMedium)
                            Spacer()
                            Text(product.description, style = MaterialTheme.typography.bodyMedium)

                            product.brand?.let {
                                Spacer()
                                Text(
                                    stringResource(R.string.detail_brand, it),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            product.warrantyInformation?.let {
                                Spacer()
                                Text(
                                    stringResource(R.string.detail_warranty, it),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            product.shippingInformation?.let {
                                Spacer()
                                Text(
                                    stringResource(R.string.detail_shipping, it),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            product.returnPolicy?.let {
                                Spacer()
                                Text(
                                    stringResource(R.string.detail_return_policy, it),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            if (state.reviews.isNotEmpty()) {
                                Spacer()
                                Text(
                                    stringResource(R.string.detail_reviews),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }

                    items(state.reviews) { review ->
                        _root_ide_package_.com.nastia.catalogapp.products.components.ReviewItem(
                            review
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Spacer() {
    androidx.compose.foundation.layout.Spacer(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    )
}