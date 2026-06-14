package com.nastia.catalogapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastia.catalogapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isBiometricEnabled: StateFlow<Boolean> = authRepository.isBiometricEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null, loginError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, loginError = null) }
    }

    fun login() {
        val state = _uiState.value
        val usernameError = if (state.username.isBlank()) "Username is required" else null
        val passwordError = when {
            state.password.isBlank() -> "Password is required"
            state.password.length < 4 -> "Password must be at least 4 characters"
            else -> null
        }

        if (usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(usernameError = usernameError, passwordError = passwordError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }
            val result = authRepository.login(state.username, state.password)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, loginError = error.message ?: "Login failed")
                    }
                }
            )
        }
    }

    fun onBiometricLoginSuccess() {
        _uiState.update { it.copy(loginSuccess = true) }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            authRepository.setBiometricEnabled(enabled)
        }
    }
}