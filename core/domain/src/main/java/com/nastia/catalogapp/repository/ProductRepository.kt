package com.nastia.catalogapp.repository

import androidx.paging.PagingData
import com.nastia.catalogapp.model.Product
import com.nastia.catalogapp.model.ProductSort
import com.nastia.catalogapp.model.Review
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProducts(
        category: String? = null,
        searchQuery: String? = null,
        sortBy: ProductSort = ProductSort.DEFAULT
    ): Flow<PagingData<Product>>

    fun getProductById(id: Int): Flow<Product?>

    fun getReviewsForProduct(productId: Int): Flow<List<Review>>

    fun getCategories(): Flow<List<String>>

    // CRUD
    suspend fun createProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(id: Int)
    suspend fun resetLocalChanges()
}