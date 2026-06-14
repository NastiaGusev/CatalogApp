package com.nastia.catalogapp.ui.crud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastia.catalogapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productId: Int?,
    onProductUpdated: () -> Unit,
    onProductDeleted: () -> Unit,
    viewModel: AddEditProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved, uiState.isDeleted) {
        if (uiState.isSaved) {
            onProductUpdated()
        }
        if (uiState.isDeleted) {
            onProductDeleted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(if (uiState.isEditMode) R.string.crud_edit_title else R.string.crud_add_title)) },
                navigationIcon = {
                    IconButton(onClick = onProductUpdated) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.detail_back))
                    }
                },
                actions = {
                    if (uiState.isEditMode) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.crud_delete))
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text(stringResource(R.string.crud_field_title)) },
                isError = uiState.titleError != null,
                supportingText = { uiState.titleError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text(stringResource(R.string.crud_field_description)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            OutlinedTextField(
                value = uiState.category,
                onValueChange = viewModel::onCategoryChange,
                label = { Text(stringResource(R.string.crud_field_category)) },
                isError = uiState.categoryError != null,
                supportingText = { uiState.categoryError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            OutlinedTextField(
                value = uiState.price,
                onValueChange = viewModel::onPriceChange,
                label = { Text(stringResource(R.string.crud_field_price)) },
                isError = uiState.priceError != null,
                supportingText = { uiState.priceError?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            OutlinedTextField(
                value = uiState.stock,
                onValueChange = viewModel::onStockChange,
                label = { Text(stringResource(R.string.crud_field_stock)) },
                isError = uiState.stockError != null,
                supportingText = { uiState.stockError?.let { Text(it) } },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            OutlinedTextField(
                value = uiState.brand,
                onValueChange = viewModel::onBrandChange,
                label = { Text(stringResource(R.string.crud_field_brand)) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            OutlinedTextField(
                value = uiState.thumbnail,
                onValueChange = viewModel::onThumbnailChange,
                label = { Text(stringResource(R.string.crud_field_image_url)) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            Button(
                onClick = { viewModel.save() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text(stringResource(if (uiState.isEditMode) R.string.crud_save else R.string.crud_add))
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.crud_delete_title)) },
            text = { Text(stringResource(R.string.crud_delete_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete()
                    showDeleteDialog = false
                }) { Text(stringResource(R.string.crud_delete_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(R.string.settings_cancel)) }
            }
        )
    }
}