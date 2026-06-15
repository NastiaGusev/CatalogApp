package com.nastia.catalogapp.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nastia.catalogapp.model.Product
import com.nastia.catalogapp.model.ProductSort
import com.nastia.catalogapp.repository.FavoritesRepository
import com.nastia.catalogapp.repository.ProductRepository
import com.nastia.catalogapp.util.CatalogRefreshSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoritesRepository: FavoritesRepository,
    val catalogRefreshSignal: CatalogRefreshSignal
) : ViewModel() {

    private val _filters = MutableStateFlow(ProductListFilters())
    val filters: StateFlow<ProductListFilters> = _filters.asStateFlow()

    val categories: StateFlow<List<String>> = productRepository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteIds: StateFlow<Set<Int>> = favoritesRepository.getFavoriteIds()
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val products: Flow<PagingData<Product>> = _filters
        .flatMapLatest { filters ->
            productRepository.getProducts(
                category = filters.selectedCategory,
                searchQuery = filters.searchQuery.ifBlank { null },
                sortBy = filters.sortBy
            )
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _filters.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: String?) {
        _filters.update { it.copy(selectedCategory = category) }
    }

    fun onSortSelected(sort: ProductSort) {
        _filters.update { it.copy(sortBy = sort) }
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            val isFav = favoriteIds.value.contains(productId)
            if (isFav) {
                favoritesRepository.removeFavorite(productId)
            } else {
                favoritesRepository.addFavorite(productId)
            }
        }
    }

    fun refreshAfterReset() {
        _filters.update { it.copy(refreshTrigger = it.refreshTrigger + 1) }
    }
}