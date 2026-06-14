package com.nastia.catalogapp.domain.repository

import com.nastia.catalogapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteIds(): Flow<List<Int>>
    fun isFavorite(productId: Int): Flow<Boolean>
    fun getFavoriteProducts(): Flow<List<Product>>
    suspend fun addFavorite(productId: Int)
    suspend fun removeFavorite(productId: Int)
}