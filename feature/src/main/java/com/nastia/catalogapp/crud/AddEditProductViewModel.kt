package com.nastia.catalogapp.crud

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nastia.catalogapp.model.Product
import com.nastia.catalogapp.repository.ProductRepository
import com.nastia.catalogapp.usecase.ValidateProductInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val validateProductInput: ValidateProductInputUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<com.nastia.catalogapp.navigation.NavRoutes.AddEditProduct>()

    private val _uiState = MutableStateFlow(AddEditProductUiState(productId = args.productId))
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    init {
        args.productId?.let { id ->
            viewModelScope.launch {
                val product = productRepository.getProductById(id).first()
                product?.let { p ->
                    _uiState.update {
                        it.copy(
                            originalProduct = p,
                            title = p.title,
                            description = p.description,
                            category = p.category,
                            price = p.price.toString(),
                            stock = p.stock.toString(),
                            brand = p.brand ?: "",
                            thumbnail = p.thumbnail
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, titleError = null) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }
    fun onCategoryChange(value: String) = _uiState.update { it.copy(category = value, categoryError = null) }
    fun onPriceChange(value: String) = _uiState.update { it.copy(price = value, priceError = null) }
    fun onStockChange(value: String) = _uiState.update { it.copy(stock = value, stockError = null) }
    fun onBrandChange(value: String) = _uiState.update { it.copy(brand = value) }
    fun onThumbnailChange(value: String) = _uiState.update { it.copy(thumbnail = value) }

    fun save() {
        val state = _uiState.value
        val validation = validateProductInput(state.title, state.category, state.price, state.stock)

        if (!validation.isValid) {
            _uiState.update {
                it.copy(
                    titleError = validation.titleError,
                    categoryError = validation.categoryError,
                    priceError = validation.priceError,
                    stockError = validation.stockError
                )
            }
            return
        }

        val priceValue = validation.priceValue!!
        val stockValue = validation.stockValue!!

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val product = if (state.isEditMode && state.originalProduct != null) {
                state.originalProduct.copy(
                    title = state.title,
                    description = state.description,
                    category = state.category,
                    price = priceValue,
                    stock = stockValue,
                    brand = state.brand.ifBlank { null },
                    thumbnail = state.thumbnail.ifBlank { state.originalProduct.thumbnail },
                    images = if (state.thumbnail.isNotBlank() && state.thumbnail != state.originalProduct.thumbnail)
                        listOf(state.thumbnail) else state.originalProduct.images
                )
            } else {
                Product(
                    id = 0,
                    title = state.title,
                    description = state.description,
                    category = state.category,
                    price = priceValue,
                    discountPercentage = 0.0,
                    rating = 0.0,
                    stock = stockValue,
                    brand = state.brand.ifBlank { null },
                    thumbnail = state.thumbnail.ifBlank {
                        "https://cdn.dummyjson.com/product-images/placeholder.jpg"
                    },
                    images = if (state.thumbnail.isNotBlank()) listOf(state.thumbnail) else emptyList()
                )
            }

            if (state.isEditMode) {
                productRepository.updateProduct(product)
            } else {
                productRepository.createProduct(product)
            }

            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }

    fun delete() {
        val id = _uiState.value.productId ?: return
        viewModelScope.launch {
            productRepository.deleteProduct(id)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}