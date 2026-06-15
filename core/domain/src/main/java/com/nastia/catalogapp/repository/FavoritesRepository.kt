package com.nastia.catalogapp.repository

import com.nastia.catalogapp.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteIds(): Flow<List<Int>>
    fun isFavorite(productId: Int): Flow<Boolean>
    fun getFavoriteProducts(): Flow<List<Product>>
    suspend fun addFavorite(productId: Int)
    suspend fun removeFavorite(productId: Int)
}