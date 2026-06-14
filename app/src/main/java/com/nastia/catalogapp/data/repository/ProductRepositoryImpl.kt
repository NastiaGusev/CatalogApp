package com.nastia.catalogapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nastia.catalogapp.data.local.CatalogDatabase
import com.nastia.catalogapp.data.mapper.toDomain
import com.nastia.catalogapp.data.mapper.toEntity
import com.nastia.catalogapp.data.remote.api.ProductApi
import com.nastia.catalogapp.domain.model.Product
import com.nastia.catalogapp.domain.model.ProductSort
import com.nastia.catalogapp.domain.model.Review
import com.nastia.catalogapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val db: CatalogDatabase
) : ProductRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getProducts(
        category: String?,
        searchQuery: String?,
        sortBy: ProductSort
    ): Flow<PagingData<Product>> {
        val pagingSourceFactory = {
            db.productDao().getProductsPaged(
                category = category,
                searchQuery = searchQuery,
                sortBy = sortBy.apiValue
            )
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = ProductRemoteMediator(
                api = api,
                db = db,
                category = category,
                searchQuery = searchQuery
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getProductById(id: Int): Flow<Product?> {
        return db.productDao().getProductById(id).map { it?.toDomain() }
    }

    override fun getReviewsForProduct(productId: Int): Flow<List<Review>> {
        return db.reviewDao().getReviewsForProduct(productId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getCategories(): Flow<List<String>> {
        return db.productDao().getCategories()
    }

    override suspend fun createProduct(product: Product) {
        val nextId = (db.productDao().getCount() + 100000) // avoid collision with API ids
        val entity = product.copy(id = nextId, isLocallyCreated = true, isLocallyModified = true)
            .toEntity(pageOrder = -1) // show local items first
        db.productDao().upsert(entity)
    }

    override suspend fun updateProduct(product: Product) {
        val existing = db.productDao().getProductByIdSync(product.id)
        val entity = product
            .copy(isLocallyModified = true)
            .toEntity(pageOrder = existing?.pageOrder ?: Int.MAX_VALUE)
            .copy(isLocallyCreated = existing?.isLocallyCreated ?: false)
        db.productDao().upsert(entity)
    }

    override suspend fun deleteProduct(id: Int) {
        val existing = db.productDao().getProductByIdSync(id)
        if (existing?.isLocallyCreated == true) {
            db.productDao().deleteById(id)
        } else {
            db.productDao().markDeleted(id)
        }
    }

    override suspend fun resetLocalChanges() {
        db.productDao().deleteLocallyCreatedProducts()
        db.productDao().resetLocalChangesFlags()
    }
}