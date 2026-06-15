package com.nastia.catalogapp.data.mapper

import com.nastia.catalogapp.data.local.entity.ProductEntity
import com.nastia.catalogapp.data.local.entity.ReviewEntity
import com.nastia.catalogapp.data.remote.dto.ProductDto
import com.nastia.catalogapp.data.remote.dto.ReviewDto
import com.nastia.catalogapp.domain.model.Dimensions
import com.nastia.catalogapp.domain.model.Product
import com.nastia.catalogapp.domain.model.Review

// DTO -> Entity
fun ProductDto.toEntity(pageOrder: Int = Int.MAX_VALUE): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
        thumbnail = thumbnail,
        width = dimensions?.width,
        height = dimensions?.height,
        depth = dimensions?.depth,
        pageOrder = pageOrder
    )
}

// DTO Review -> Entity
fun ReviewDto.toEntity(productId: Int): ReviewEntity {
    return ReviewEntity(
        productId = productId,
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName,
        reviewerEmail = reviewerEmail
    )
}

// Entity -> Domain
fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
        thumbnail = thumbnail,
        dimensions = if (width != null && height != null && depth != null) {
            Dimensions(width, height, depth)
        } else null,
        isLocallyModified = isLocallyModified,
        isLocallyCreated = isLocallyCreated
    )
}

// Entity Review -> Domain
fun ReviewEntity.toDomain(): Review {
    return Review(
        productId = productId,
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName,
        reviewerEmail = reviewerEmail
    )
}

// Domain -> Entity (for local CRUD edits)
fun Product.toEntity(pageOrder: Int = Int.MAX_VALUE): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
        thumbnail = thumbnail,
        width = dimensions?.width,
        height = dimensions?.height,
        depth = dimensions?.depth,
        isLocallyModified = isLocallyModified,
        isLocallyCreated = isLocallyCreated,
        pageOrder = pageOrder
    )
}