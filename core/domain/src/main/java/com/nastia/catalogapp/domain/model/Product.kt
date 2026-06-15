package com.nastia.catalogapp.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String> = emptyList(),
    val brand: String? = null,
    val sku: String? = null,
    val weight: Double? = null,
    val warrantyInformation: String? = null,
    val shippingInformation: String? = null,
    val availabilityStatus: String? = null,
    val returnPolicy: String? = null,
    val minimumOrderQuantity: Int? = null,
    val images: List<String> = emptyList(),
    val thumbnail: String,
    val dimensions: Dimensions? = null,
    val isLocallyModified: Boolean = false,
    val isLocallyCreated: Boolean = false
) {
    val discountedPrice: Double
        get() = price * (1 - discountPercentage / 100)
}

data class Dimensions(
    val width: Double,
    val height: Double,
    val depth: Double
)