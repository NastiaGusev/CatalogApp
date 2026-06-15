package com.nastia.catalogapp.di

import com.nastia.catalogapp.data.repository.AuthRepositoryImpl
import com.nastia.catalogapp.data.repository.FavoritesRepositoryImpl
import com.nastia.catalogapp.data.repository.ProductRepositoryImpl
import com.nastia.catalogapp.data.repository.SettingsRepositoryImpl
import com.nastia.catalogapp.repository.AuthRepository
import com.nastia.catalogapp.repository.FavoritesRepository
import com.nastia.catalogapp.repository.ProductRepository
import com.nastia.catalogapp.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}