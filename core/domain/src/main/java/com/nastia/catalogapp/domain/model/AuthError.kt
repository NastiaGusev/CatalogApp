package com.nastia.catalogapp.domain.model

enum class AuthError {
    USERNAME_REQUIRED,
    PASSWORD_REQUIRED,
    PASSWORD_TOO_SHORT,
    INVALID_CREDENTIALS
}