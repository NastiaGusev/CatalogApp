package com.nastia.catalogapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nastia.catalogapp.navigation.CatalogNavHost

@Composable
fun AppRoot() {
    val settingsViewModel: com.nastia.catalogapp.settings.SettingsViewModel = hiltViewModel()
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val darkTheme = uiState.isDarkMode ?: isSystemInDarkTheme()

    _root_ide_package_.com.nastia.catalogapp.theme.CatalogAppTheme(darkTheme = darkTheme) {
        CatalogNavHost()
    }
}