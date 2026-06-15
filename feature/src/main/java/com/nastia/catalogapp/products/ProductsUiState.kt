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

data class ProductDetailUiState(
    val product: Product? = null,
    val reviews: List<Review> = emptyList(),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true
)