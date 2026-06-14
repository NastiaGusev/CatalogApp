package com.nastia.catalogapp.data.local.entity

import androidx.room.Entity

@Entity(tableName = "reviews", primaryKeys = ["productId", "reviewerEmail", "date"])
data class ReviewEntity(
    val productId: Int,
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)