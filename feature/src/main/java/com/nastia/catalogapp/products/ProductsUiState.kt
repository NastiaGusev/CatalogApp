package com.nastia.catalogapp.products

import com.nastia.catalogapp.model.Product
import com.nastia.catalogapp.model.ProductSort
import com.nastia.catalogapp.model.Review

data class ProductListFilters(
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val sortBy: ProductSort = ProductSort.DEFAULT,
    val refreshTrigger: Int = 0
)

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState

    data class Success(
        val product: Product,
        val reviews: List<Review>,
        val isFavorite: Boolean
    ) : ProductDetailUiState

    data object NotFound : ProductDetailUiState
}