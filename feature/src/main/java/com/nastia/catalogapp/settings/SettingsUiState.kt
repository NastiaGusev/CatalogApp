package com.nastia.catalogapp.settings

data class SettingsUiState(
    val isDarkMode: Boolean? = null,
    val language: String = "en",
    val isBiometricEnabled: Boolean = false
)