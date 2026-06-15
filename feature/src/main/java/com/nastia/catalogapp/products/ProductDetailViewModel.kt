package com.nastia.catalogapp.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nastia.catalogapp.repository.FavoritesRepository
import com.nastia.catalogapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    productRepository: ProductRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val productId: Int =
        savedStateHandle.toRoute<com.nastia.catalogapp.navigation.NavRoutes.ProductDetail>().productId

    val uiState: StateFlow<ProductDetailUiState> = combine(
        productRepository.getProductById(productId),
        productRepository.getReviewsForProduct(productId),
        favoritesRepository.isFavorite(productId)
    ) { product, reviews, isFavorite ->
        ProductDetailUiState(
            product = product,
            reviews = reviews,
            isFavorite = isFavorite,
            isLoading = product == null
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductDetailUiState())

    fun toggleFavorite() {
        viewModelScope.launch {
            val current = uiState.value.isFavorite
            if (current) {
                favoritesRepository.removeFavorite(productId)
            } else {
                favoritesRepository.addFavorite(productId)
            }
        }
    }
}