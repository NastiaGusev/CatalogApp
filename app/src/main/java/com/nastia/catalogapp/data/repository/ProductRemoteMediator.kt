package com.nastia.catalogapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nastia.catalogapp.data.local.CatalogDatabase
import com.nastia.catalogapp.data.local.entity.ProductEntity
import com.nastia.catalogapp.data.local.entity.RemoteKeysEntity
import com.nastia.catalogapp.data.mapper.toEntity
import com.nastia.catalogapp.data.remote.api.ProductApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val api: ProductApi,
    private val db: CatalogDatabase,
    private val category: String?,
    private val searchQuery: String?
) : RemoteMediator<Int, ProductEntity>() {

    private val remoteKeyLabel: String
        get() = "products_${category ?: "all"}_${searchQuery ?: "none"}"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val pageSize = state.config.pageSize

            val skip = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = db.remoteKeysDao().getRemoteKeys(remoteKeyLabel)
                    if (remoteKeys?.isEndReached == true) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeys?.nextSkip
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // Only hit the network for "browse all" or category queries.
            // Search is handled separately (not paginated via mediator) — see note below.
            val response = when {
                !searchQuery.isNullOrBlank() -> api.searchProducts(searchQuery, pageSize, skip)
                !category.isNullOrBlank() -> api.getProductsByCategory(category, pageSize, skip)
                else -> api.getProducts(pageSize, skip)
            }

            val products = response.products
            val endOfPaginationReached = (skip + products.size) >= response.total

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Only clear products that belong to this query's window and aren't
                    // locally modified/created, to preserve local CRUD edits.
                    db.productDao().clearUnmodifiedProducts()
                    db.remoteKeysDao().deleteByLabel(remoteKeyLabel)
                }

                val entities = products.mapIndexed { index, dto ->
                    dto.toEntity(pageOrder = skip + index)
                }
                db.productDao().upsertAll(entities)

                // store reviews too
                products.forEach { dto ->
                    if (dto.reviews.isNotEmpty()) {
                        db.reviewDao().upsertAll(dto.reviews.map { it.toEntity(dto.id) })
                    }
                }

                val nextSkip = if (endOfPaginationReached) null else skip + pageSize
                db.remoteKeysDao().upsert(
                    RemoteKeysEntity(
                        label = remoteKeyLabel,
                        nextSkip = nextSkip,
                        isEndReached = endOfPaginationReached
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}