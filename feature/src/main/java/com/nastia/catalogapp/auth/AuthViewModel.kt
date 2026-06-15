package com.nastia.catalogapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nastia.catalogapp.model.AuthError
import com.nastia.catalogapp.repository.AuthException
import com.nastia.catalogapp.repository.AuthRepository
import com.nastia.catalogapp.usecase.ValidateLoginInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateLoginInput: ValidateLoginInputUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _hasLoadedSession = MutableStateFlow(false)
    val hasLoadedSession: StateFlow<Boolean> = _hasLoadedSession.asStateFlow()

    val sessionState: StateFlow<SessionState> = combine(
        authRepository.isLoggedIn,
        authRepository.isBiometricEnabled
    ) { loggedIn, biometricEnabled ->
        _hasLoadedSession.value = true
        SessionState(loggedIn, biometricEnabled)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SessionState(isLoggedIn = false, isBiometricEnabled = false)
    )

    fun login() {
        val state = _uiState.value
        val validation = validateLoginInput(state.username, state.password)

        if (!validation.isValid) {
            _uiState.update {
                it.copy(
                    usernameError = validation.usernameError,
                    passwordError = validation.passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, formError = null) }
            val result = authRepository.login(state.username, state.password)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                },
                onFailure = { exception ->
                    when (exception) {
                        is AuthException -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    formError = exception.error
                                )
                            }
                        }

                        else -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    formError = AuthError.INVALID_CREDENTIALS
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null, formError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, formError = null) }
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