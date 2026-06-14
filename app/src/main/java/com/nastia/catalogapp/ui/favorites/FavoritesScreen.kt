package com.nastia.catalogapp.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastia.catalogapp.R
import com.nastia.catalogapp.ui.favorites.components.FavoriteListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onProductClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val pendingRemoval by viewModel.pendingRemoval.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val removedMessage = pendingRemoval?.let { stringResource(R.string.favorites_removed, it.title) } ?: ""
    val undoLabel = stringResource(R.string.favorites_undo)

    LaunchedEffect(pendingRemoval) {
        if (pendingRemoval == null) return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = removedMessage,
            actionLabel = undoLabel,
            duration = SnackbarDuration.Short
        )
        when (result) {
            SnackbarResult.ActionPerformed -> viewModel.undoRemove()
            SnackbarResult.Dismissed -> viewModel.clearPendingRemoval()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (favorites.isEmpty() && pendingRemoval == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.favorites_empty_title), style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(R.string.favorites_empty_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites, key = { it.id }) { product ->
                    FavoriteListItem(
                        product = product,
                        onClick = { onProductClick(product.id) },
                        onRemove = { viewModel.removeFavorite(product) }
                    )
                }
            }
        }
    }
}