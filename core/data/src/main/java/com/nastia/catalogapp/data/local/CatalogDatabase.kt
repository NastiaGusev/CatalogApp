package com.nastia.catalogapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nastia.catalogapp.data.local.dao.FavoriteDao
import com.nastia.catalogapp.data.local.dao.ProductDao
import com.nastia.catalogapp.data.local.dao.RemoteKeysDao
import com.nastia.catalogapp.data.local.dao.ReviewDao
import com.nastia.catalogapp.data.local.entity.FavoriteEntity
import com.nastia.catalogapp.data.local.entity.ProductEntity
import com.nastia.catalogapp.data.local.entity.RemoteKeysEntity
import com.nastia.catalogapp.data.local.entity.ReviewEntity

@Database(
    entities = [
        ProductEntity::class,
        FavoriteEntity::class,
        ReviewEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CatalogDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun reviewDao(): ReviewDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}