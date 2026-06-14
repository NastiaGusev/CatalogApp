package com.nastia.catalogapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: NavRoutes,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(NavRoutes.ProductList, "Products", Icons.Filled.Home),
    BottomNavItem(NavRoutes.Favorites, "Favorites", Icons.Filled.Favorite),
    BottomNavItem(NavRoutes.Settings, "Settings", Icons.Filled.Settings)
)