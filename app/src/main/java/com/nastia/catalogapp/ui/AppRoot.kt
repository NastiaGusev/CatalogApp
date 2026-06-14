package com.nastia.catalogapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastia.catalogapp.ui.navigation.CatalogNavHost
import com.nastia.catalogapp.ui.settings.SettingsViewModel
import com.nastia.catalogapp.ui.theme.CatalogAppTheme

@Composable
fun AppRoot() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val darkTheme = uiState.isDarkMode ?: isSystemInDarkTheme()

    CatalogAppTheme(darkTheme = darkTheme) {
        CatalogNavHost()
    }
}