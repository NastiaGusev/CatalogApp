package com.nastia.catalogapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.nastia.catalogapp.R

data class BottomNavItem(
    val route: NavRoutes,
    @StringRes val labelRes: Int,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(NavRoutes.ProductList, R.string.nav_products, Icons.Filled.Home),
    BottomNavItem(NavRoutes.Favorites, R.string.nav_favorites, Icons.Filled.Favorite),
    BottomNavItem(NavRoutes.Settings, R.string.nav_settings, Icons.Filled.Settings)
)