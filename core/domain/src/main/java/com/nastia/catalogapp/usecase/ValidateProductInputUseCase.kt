package com.nastia.catalogapp.usecase

import com.nastia.catalogapp.model.ProductError
import javax.inject.Inject

class ValidateProductInputUseCase @Inject constructor() {
    operator fun invoke(
        title: String,
        category: String,
        price: String,
        stock: String
    ): ProductValidationResult {
        val titleError = if (title.isBlank()) ProductError.TITLE_REQUIRED else null
        val categoryError = if (category.isBlank()) ProductError.CATEGORY_REQUIRED else null

        val priceValue = price.toDoubleOrNull()
        val priceError = when {
            price.isBlank() -> ProductError.PRICE_REQUIRED
            priceValue == null -> ProductError.PRICE_INVALID
            priceValue < 0 -> ProductError.PRICE_NEGATIVE
            else -> null
        }

        val stockValue = stock.toIntOrNull()
        val stockError = when {
            stock.isBlank() -> ProductError.STOCK_REQUIRED
            stockValue == null -> ProductError.STOCK_INVALID
            stockValue < 0 -> ProductError.STOCK_NEGATIVE
            else -> null
        }

        return ProductValidationResult(
            titleError = titleError,
            categoryError = categoryError,
            priceError = priceError,
            stockError = stockError,
            priceValue = priceValue,
            stockValue = stockValue
        )
    }
}

data class ProductValidationResult(
    val titleError: ProductError?,
    val categoryError: ProductError?,
    val priceError: ProductError?,
    val stockError: ProductError?,
    val priceValue: Double?,
    val stockValue: Int?
) {
    val isValid: Boolean
        get() = titleError == null && categoryError == null && priceError == null && stockError == null
}