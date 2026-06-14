package com.nastia.catalogapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastia.catalogapp.domain.model.Product
import com.nastia.catalogapp.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val favorites: StateFlow<List<Product>> = favoritesRepository.getFavoriteProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _pendingRemoval = MutableStateFlow<Product?>(null)
    val pendingRemoval: StateFlow<Product?> = _pendingRemoval.asStateFlow()

    fun removeFavorite(product: Product) {
        _pendingRemoval.value = product
        viewModelScope.launch {
            favoritesRepository.removeFavorite(product.id)
        }
    }

    fun undoRemove() {
        val product = _pendingRemoval.value ?: return
        viewModelScope.launch {
            favoritesRepository.addFavorite(product.id)
            _pendingRemoval.value = null
        }
    }

    fun clearPendingRemoval() {
        _pendingRemoval.value = null
    }
}