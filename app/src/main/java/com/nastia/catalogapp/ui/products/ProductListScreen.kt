package com.nastia.catalogapp.ui.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.nastia.catalogapp.ui.products.components.CategoryFilterRow
import com.nastia.catalogapp.ui.products.components.ProductGridContent
import com.nastia.catalogapp.ui.products.components.ProductSearchBar
import com.nastia.catalogapp.ui.products.components.SortDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onAddProduct: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val resetVersion by viewModel.catalogRefreshSignal.resetVersion.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    val isRefreshing = products.loadState.refresh is androidx.paging.LoadState.Loading

    LaunchedEffect(resetVersion) {
        if (resetVersion > 0) {
            viewModel.refreshAfterReset()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProduct) {
                Icon(Icons.Filled.Add, contentDescription = "Add product")
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { products.refresh() },
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                ProductSearchBar(
                    query = filters.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )
                SortDropdown(
                    selected = filters.sortBy,
                    onSortSelected = viewModel::onSortSelected
                )
                CategoryFilterRow(
                    categories = categories,
                    selectedCategory = filters.selectedCategory,
                    onCategorySelected = viewModel::onCategorySelected
                )
                ProductGridContent(
                    products = products,
                    favoriteIds = favoriteIds,
                    onProductClick = onProductClick,
                    onFavoriteClick = viewModel::toggleFavorite,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}