package com.nastia.catalogapp.usecase

import com.nastia.catalogapp.model.AuthError
import javax.inject.Inject

class ValidateLoginInputUseCase @Inject constructor() {
    operator fun invoke(username: String, password: String): ValidationResult {
        val usernameError = if (username.isBlank()) AuthError.USERNAME_REQUIRED else null
        val passwordError = when {
            password.isBlank() -> AuthError.PASSWORD_REQUIRED
            password.length < 4 -> AuthError.PASSWORD_TOO_SHORT
            else -> null
        }
        return ValidationResult(usernameError, passwordError)
    }
}

data class ValidationResult(
    val usernameError: AuthError?,
    val passwordError: AuthError?
) {
    val isValid: Boolean get() = usernameError == null && passwordError == null
}