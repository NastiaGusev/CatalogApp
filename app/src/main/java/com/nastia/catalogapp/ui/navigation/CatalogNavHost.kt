package com.nastia.catalogapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nastia.catalogapp.ui.auth.AuthViewModel
import com.nastia.catalogapp.ui.auth.LoginScreen
import com.nastia.catalogapp.ui.crud.AddEditProductScreen
import com.nastia.catalogapp.ui.favorites.FavoritesScreen
import com.nastia.catalogapp.ui.products.ProductDetailScreen
import com.nastia.catalogapp.ui.products.ProductListScreen
import com.nastia.catalogapp.ui.settings.SettingsScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CatalogNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    val startGraph = if (isLoggedIn) NavRoutes.MainGraph else NavRoutes.AuthGraph

    NavHost(
        navController = navController,
        startDestination = startGraph
    ) {
        // ---- Auth graph ----
        navigation<NavRoutes.AuthGraph>(startDestination = NavRoutes.Login) {
            composable<NavRoutes.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(NavRoutes.MainGraph) {
                            popUpTo(NavRoutes.AuthGraph) { inclusive = true }
                        }
                    }
                )
            }
        }

        // ---- Main graph (with bottom nav) ----
        navigation<NavRoutes.MainGraph>(startDestination = NavRoutes.ProductList) {

            composable<NavRoutes.ProductList> {
                MainScaffold(navController) {
                    ProductListScreen(
                        onProductClick = { id ->
                            navController.navigate(NavRoutes.ProductDetail(id))
                        },
                        onNavigateToFavorites = { navController.navigate(NavRoutes.Favorites) },
                        onNavigateToSettings = { navController.navigate(NavRoutes.Settings) },
                        onAddProduct = { navController.navigate(NavRoutes.AddEditProduct()) }
                    )
                }
            }

            composable<NavRoutes.ProductDetail> { backStackEntry ->
                val args = backStackEntry.toRoute<NavRoutes.ProductDetail>()
                ProductDetailScreen(
                    productId = args.productId,
                    onNavigateUp = { navController.navigateUp() },
                    onEditProduct = { id ->
                        navController.navigate(NavRoutes.AddEditProduct(productId = id))
                    }
                )
            }

            composable<NavRoutes.Favorites> {
                MainScaffold(navController) {
                    FavoritesScreen(
                        onProductClick = { id ->
                            navController.navigate(NavRoutes.ProductDetail(id))
                        }
                    )
                }
            }

            composable<NavRoutes.Settings> {
                MainScaffold(navController) {
                    SettingsScreen(
                        onLogout = {
                            navController.navigate(NavRoutes.AuthGraph) {
                                popUpTo(NavRoutes.MainGraph) { inclusive = true }
                            }
                        }
                    )
                }
            }

            composable<NavRoutes.AddEditProduct> { backStackEntry ->
                val args = backStackEntry.toRoute<NavRoutes.AddEditProduct>()
                AddEditProductScreen(
                    productId = args.productId,
                    onProductUpdated = { navController.navigateUp() },
                    onProductDeleted = {
                        navController.popBackStack(
                            route = NavRoutes.ProductList,
                            inclusive = false
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MainScaffold(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val selected =
                        currentRoute?.contains(item.route::class.simpleName ?: "") == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = stringResource(item.labelRes)
                            )
                        },
                        label = { Text(stringResource(item.labelRes)) }
                    )
                }
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}