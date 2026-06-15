package com.nastia.catalogapp.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoutes {

    // Graphs
    @Serializable
    data object AuthGraph : NavRoutes

    @Serializable
    data object MainGraph : NavRoutes

    // Auth screens
    @Serializable
    data object Login : NavRoutes

    // Main screens
    @Serializable
    data object ProductList : NavRoutes

    @Serializable
    data class ProductDetail(val productId: Int) : NavRoutes

    @Serializable
    data object Favorites : NavRoutes

    @Serializable
    data object Settings : NavRoutes

    @Serializable
    data class AddEditProduct(val productId: Int? = null) : NavRoutes
}