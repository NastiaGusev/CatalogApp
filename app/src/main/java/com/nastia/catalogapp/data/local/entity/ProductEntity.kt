package com.nastia.catalogapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nastia.catalogapp.data.local.Converters

@Entity(tableName = "products")
@TypeConverters(Converters::class)
data class ProductEntity(
    @PrimaryKey val id: Int,
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
    val width: Double? = null,
    val height: Double? = null,
    val depth: Double? = null,
    val isLocallyModified: Boolean = false,
    val isLocallyDeleted: Boolean = false,
    val isLocallyCreated: Boolean = false,
    val pageOrder: Int = Int.MAX_VALUE
)