package com.nastia.catalogapp.ui.auth

import com.nastia.catalogapp.domain.model.AuthError

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val formError: AuthError? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

data class SessionState(
    val isLoggedIn: Boolean,
    val isBiometricEnabled: Boolean
)