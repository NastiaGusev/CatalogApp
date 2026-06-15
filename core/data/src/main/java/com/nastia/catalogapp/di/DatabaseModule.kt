package com.nastia.catalogapp.di

import android.content.Context
import androidx.room.Room
import com.nastia.catalogapp.data.local.CatalogDatabase
import com.nastia.catalogapp.data.local.dao.FavoriteDao
import com.nastia.catalogapp.data.local.dao.ProductDao
import com.nastia.catalogapp.data.local.dao.RemoteKeysDao
import com.nastia.catalogapp.data.local.dao.ReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CatalogDatabase {
        return Room.databaseBuilder(
            context,
            CatalogDatabase::class.java,
            "catalog_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideProductDao(database: CatalogDatabase): ProductDao = database.productDao()

    @Provides
    fun provideFavoriteDao(database: CatalogDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideReviewDao(database: CatalogDatabase): ReviewDao = database.reviewDao()

    @Provides
    fun provideRemoteKeysDao(database: CatalogDatabase): RemoteKeysDao = database.remoteKeysDao()
}