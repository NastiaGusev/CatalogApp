package com.nastia.catalogapp.data.repository

import com.nastia.catalogapp.data.local.CatalogDatabase
import com.nastia.catalogapp.data.local.entity.FavoriteEntity
import com.nastia.catalogapp.data.mapper.toDomain
import com.nastia.catalogapp.domain.model.Product
import com.nastia.catalogapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val db: CatalogDatabase
) : FavoritesRepository {

    override fun getFavoriteIds(): Flow<List<Int>> {
        return db.favoriteDao().getFavoriteIds()
    }

    override fun isFavorite(productId: Int): Flow<Boolean> {
        return db.favoriteDao().isFavorite(productId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavoriteProducts(): Flow<List<Product>> {
        return db.favoriteDao().getFavoriteIds().flatMapLatest { ids ->
            if (ids.isEmpty()) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                combine(ids.map { id -> db.productDao().getProductById(id) }) { products ->
                    products.filterNotNull().map { it.toDomain() }
                }
            }
        }
    }

    override suspend fun addFavorite(productId: Int) {
        db.favoriteDao().addFavorite(FavoriteEntity(productId = productId))
    }

    override suspend fun removeFavorite(productId: Int) {
        db.favoriteDao().removeFavorite(productId)
    }
}