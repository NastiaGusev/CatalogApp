package com.nastia.catalogapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nastia.catalogapp.R

@Composable
fun CatalogNavHost() {
    val navController = rememberNavController()
    val authViewModel: com.nastia.catalogapp.auth.AuthViewModel = hiltViewModel()
    val sessionState by authViewModel.sessionState.collectAsStateWithLifecycle()
    val hasLoaded by authViewModel.hasLoadedSession.collectAsStateWithLifecycle()

    if (!hasLoaded) {
        _root_ide_package_.com.nastia.catalogapp.auth.LoginScreen(onLoginSuccess = {})
        return
    }

    val startDestination = remember {
        if (sessionState.isLoggedIn && !sessionState.isBiometricEnabled) {
            NavRoutes.MainGraph
        } else {
            NavRoutes.AuthGraph
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ---- Auth graph ----
        navigation<NavRoutes.AuthGraph>(startDestination = NavRoutes.Login) {
            composable<NavRoutes.Login> {
                _root_ide_package_.com.nastia.catalogapp.auth.LoginScreen(
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
                MainScaffold(navController, title = stringResource(R.string.nav_products)) {
                    _root_ide_package_.com.nastia.catalogapp.products.ProductListScreen(
                        onProductClick = { id ->
                            navController.navigate(NavRoutes.ProductDetail(id))
                        },
                        onAddProduct = { navController.navigate(NavRoutes.AddEditProduct()) }
                    )
                }
            }

            composable<NavRoutes.ProductDetail> { backStackEntry ->
                val args = backStackEntry.toRoute<NavRoutes.ProductDetail>()
                _root_ide_package_.com.nastia.catalogapp.products.ProductDetailScreen(
                    productId = args.productId,
                    onNavigateUp = { navController.navigateUp() },
                    onEditProduct = { id ->
                        navController.navigate(NavRoutes.AddEditProduct(productId = id))
                    }
                )
            }

            composable<NavRoutes.Favorites> {
                MainScaffold(navController, title = stringResource(R.string.favorites_title)) {
                    _root_ide_package_.com.nastia.catalogapp.favorites.FavoritesScreen(
                        onProductClick = { id ->
                            navController.navigate(NavRoutes.ProductDetail(id))
                        }
                    )
                }
            }

            composable<NavRoutes.Settings> {
                MainScaffold(navController, title = stringResource(R.string.settings_title)) {
                    _root_ide_package_.com.nastia.catalogapp.settings.SettingsScreen(
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
                _root_ide_package_.com.nastia.catalogapp.crud.AddEditProductScreen(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(
    navController: NavHostController,
    title: String,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
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