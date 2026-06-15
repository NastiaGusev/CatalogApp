package com.nastia.catalogapp.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.nastia.catalogapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
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

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { products.refresh() },
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                _root_ide_package_.com.nastia.catalogapp.products.components.ProductSearchBar(
                    query = filters.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )
                _root_ide_package_.com.nastia.catalogapp.products.components.SortDropdown(
                    selected = filters.sortBy,
                    onSortSelected = viewModel::onSortSelected
                )
                _root_ide_package_.com.nastia.catalogapp.products.components.CategoryFilterRow(
                    categories = categories,
                    selectedCategory = filters.selectedCategory,
                    onCategorySelected = viewModel::onCategorySelected
                )
                _root_ide_package_.com.nastia.catalogapp.products.components.ProductGridContent(
                    products = products,
                    favoriteIds = favoriteIds,
                    onProductClick = onProductClick,
                    onFavoriteClick = viewModel::toggleFavorite,
                    modifier = Modifier.weight(1f)
                )
            }
            FloatingActionButton(
                onClick = onAddProduct,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.products_add))
            }
        }
    }
}