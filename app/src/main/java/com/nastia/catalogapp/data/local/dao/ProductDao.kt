package com.nastia.catalogapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.nastia.catalogapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("""
        SELECT * FROM products 
        WHERE isLocallyDeleted = 0
        AND (:category IS NULL OR category = :category)
        AND (:searchQuery IS NULL OR title LIKE '%' || :searchQuery || '%')
        ORDER BY 
            CASE WHEN :sortBy = 'price_asc' THEN price END ASC,
            CASE WHEN :sortBy = 'price_desc' THEN price END DESC,
            CASE WHEN :sortBy = 'rating_desc' THEN rating END DESC,
            CASE WHEN :sortBy = 'name_asc' THEN title END ASC,
            pageOrder ASC
    """)
    fun getProductsPaged(
        category: String?,
        searchQuery: String?,
        sortBy: String?
    ): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Flow<ProductEntity?>

    @Query("SELECT DISTINCT category FROM products WHERE isLocallyDeleted = 0 ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Query("DELETE FROM products WHERE isLocallyCreated = 0 AND isLocallyModified = 0")
    suspend fun clearUnmodifiedProducts()

    @Query("DELETE FROM products")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int

    @Query("UPDATE products SET isLocallyDeleted = 1, isLocallyModified = 1 WHERE id = :id")
    suspend fun markDeleted(id: Int)

    @Query("""
        UPDATE products SET isLocallyDeleted = 0, isLocallyModified = 0, isLocallyCreated = 0 
        WHERE isLocallyDeleted = 1 OR isLocallyModified = 1 OR isLocallyCreated = 1
    """)
    suspend fun resetLocalChangesFlags()

    @Query("DELETE FROM products WHERE isLocallyCreated = 1")
    suspend fun deleteLocallyCreatedProducts()

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductByIdSync(id: Int): ProductEntity?

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: Int)
}