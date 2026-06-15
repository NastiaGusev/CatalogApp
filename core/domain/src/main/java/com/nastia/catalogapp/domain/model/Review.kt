package com.nastia.catalogapp.domain.model

data class Review(
    val productId: Int,
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)