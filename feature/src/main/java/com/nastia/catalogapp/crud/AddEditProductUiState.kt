package com.nastia.catalogapp.crud

import com.nastia.catalogapp.model.Product
import com.nastia.catalogapp.model.ProductError

data class AddEditProductUiState(
    val productId: Int? = null,
    val originalProduct: Product? = null,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val stock: String = "",
    val brand: String = "",
    val thumbnail: String = "",
    val titleError: ProductError? = null,
    val priceError: ProductError? = null,
    val categoryError: ProductError? = null,
    val stockError: ProductError? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
) {
    val isEditMode: Boolean get() = productId != null
}