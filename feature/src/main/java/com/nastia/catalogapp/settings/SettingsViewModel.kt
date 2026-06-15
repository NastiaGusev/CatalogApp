package com.nastia.catalogapp.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastia.catalogapp.domain.repository.AuthRepository
import com.nastia.catalogapp.domain.repository.ProductRepository
import com.nastia.catalogapp.domain.repository.SettingsRepository
import com.nastia.catalogapp.util.CatalogRefreshSignal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkMode: Boolean? = null, // null = follow system
    val language: String = "en",
    val isBiometricEnabled: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val catalogRefreshSignal: CatalogRefreshSignal
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.isDarkMode,
        settingsRepository.language,
        authRepository.isBiometricEnabled
    ) { darkMode, language, biometric ->
        SettingsUiState(
            isDarkMode = darkMode,
            language = language,
            isBiometricEnabled = biometric
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(languageCode)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            authRepository.setBiometricEnabled(enabled)
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onComplete()
        }
    }

    fun resetLocalChanges() {
        viewModelScope.launch {
            productRepository.resetLocalChanges()
            catalogRefreshSignal.notifyReset()
        }
    }
}