package com.nastia.catalogapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: Int,
    val addedAt: Long = System.currentTimeMillis()
)